
Function doExecute()
	Call showDailog("update File Path:" & resultFilePath)
	if fso.fileExists(resultFilePath)=False Then
		Call showDailog("No update plan found")
		wscript.quit
	end If

	versionInfo=getFileContent(resultFilePath,1)

	Call showDailog("versionInfo:" & versionInfo)
	
	Set versionObject=ParseJson(versionInfo)
	If versionObject="" Then
		Call showDailog("param is error")
		wscript.quit
	Else
		version=versionObject.version
	End If

	'wscript.quit
	operateRequestUrl= versionObject.domainName &"/v1/api/javaClient/updateUpdatePlan"

	javaCurrentVersion=getFileContent(javacurrentVersionPath,1)

	If versionObject.status="success" And  version=javaCurrentVersion Then
		Call showDailog("the current version is the latest version")
		wscript.quit
	end If
	'Send start request command
	url = myRequestUrl("start",versionObject)
	Set requestResult=HttpRequest(url)
	code =requestResult.readystate
	If code=4 Then
		Set resultObject=ParseJson(requestResult.responsetext)
		'MsgBox resultObject.result
		If resultObject.result =200 Then
			requestCode=1
		ELSE
			requestCode=0
		END IF
	Else
		requestCode=0
	end if
	Call setResultToFile(versionObject.id,versionObject.version,versionObject.machineNum,"start",requestCode,versionObject.domainName,versionObject.updateDate)
	Call doUpdateExecute(versionObject)
	
End Function



Function doUpdateExecute(currentVersionObject)

	Call killProcess
	
	version=currentVersionObject.version
	Call executeUpdateShell(version)

	WScript.Sleep 10000
	strComputer = "."
	Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
	Set colProcessList = objWMIService.ExecQuery("Select * from Win32_Process Where Name = 'java.exe'")
	If colProcessList.Count>0 Then
		Set requestContent=HttpRequest(checkJavaIsOkUrl)
		code =requestContent.readystate
		If code=4 Then
			Call SendSuccessRequestToServer("success",currentVersionObject)
			Call writeContentFile(javacurrentVersionPath,version)
		Else
			Call showDailog("Java cannot be accessed after update")
			Call SendFailRequestToServer("error",currentVersionObject)
		end If
	Else
		Call showDailog("Java does not start after update")
		Call SendFailRequestToServer("error",currentVersionObject)
	End If
End Function

Function executeUpdateShell(version)
	ws.run javaUpdateShell & " " &version
End Function

Function SendFailRequestToServer(param,versionObject)

	url = myRequestUrl(param,versionObject)

	Set requestResult=HttpRequest(url)
	code =requestResult.readystate
	'if code is 4, then this request is ok

	If code=4 Then
		Set  resultObject=ParseJson(requestResult.responsetext)
		'MsgBox resultObject.result
		If resultObject.result =200 Then
			requestCode=1
		Else
			requestCode=0
		End If
	Else
		requestCode=0
	End If

	Call setResultToFile(versionObject.id,versionObject.version,versionObject.machineNum,param,requestCode,versionObject.domainName,versionObject.updateDate)

	Call showDailog("Execute rollback")
	
	Call rollBack
End Function

Function rollBack()
	
	Call killProcess
	
	ws.run javaRollBackShell
	
	WScript.Sleep 10000
	strComputer = "."
	Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
	Set colProcessList = objWMIService.ExecQuery("Select * from Win32_Process Where Name = 'java.exe'")
	If colProcessList.Count>0 Then
		Set requestContent=HttpRequest(checkJavaIsOkUrl)
		code =requestContent.readystate
		If code=4 Then
			
		Else
			Call showDailog("Java cannot be accessed after rollback")
			wscript.quit
		end If
	Else
		Call showDailog("Java does not start after rollback")
		wscript.quit
	End If
End Function

Function SendSuccessRequestToServer(param,versionObject)
	url = myRequestUrl(param,versionObject)
	Set requestResult=HttpRequest(url)
	code =requestResult.readystate
	'if code is 4, then this request is ok
	If code=4 Then
		Set  resultObject=ParseJson(requestResult.responsetext)
		'MsgBox resultObject.result
		If resultObject.result =200 Then
			requestCode=1
		Else
			requestCode=0
		End If
	Else
		requestCode=0
	End If

	Call setResultToFile(versionObject.id,versionObject.version,versionObject.machineNum,param,requestCode,versionObject.domainName,versionObject.updateDate)
End Function


Function writeContentFile(filePath,content)
	Set file = fso.OpenTextFile(filePath, 2,true)
	file.write(content)
	file.close
End Function

Function getFileContent(filePath,readOrwrite)
	  Set file = fso.OpenTextFile(filePath, readOrwrite, false)
	  On Error Resume Next
	  readfile=file.readall
	  file.close
	  Set file=Nothing
	  getFileContent=readfile
End Function