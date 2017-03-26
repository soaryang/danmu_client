Function doExecute()
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

	operateRequestUrl=versionObject.domainName &"/v1/api/javaClient/updateUpdatePlan"

	status=versionObject.status

	flashcurrentVersion=getFileContent(flashcurrentVersionPath,1)

	If versionObject.status="success" And  version=flashcurrentVersion Then
		Call showDailog("the current version is the latest version")
		wscript.quit
	end If
	url = myRequestUrl("start",versionObject)
	Set requestResult=HttpRequest(url)
	code =requestResult.readystate
	If code=4 Then
		Set  resultObject=ParseJson(requestResult.responsetext)
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

Function doUpdateExecute(versionObject)
	
	Call killProcess
	
	version=versionObject.version
	Call showDailog("version:" & version)
	Call executeFlashUpdate(version)

	WScript.Sleep 20000
	If checkJavaIsStart=1 Then
		Call checkFlashIsStart(versionObject)
	ElseIf checkJavaIsStart=2 Then
		Call showDailog("Java cannot be accessed after update")
		Call SendFailRequestToServer("error",versionObject)
	ElseIf checkJavaIsStart=3 Then
		Call showDailog("Java does not start after update")
		Call SendFailRequestToServer("error",versionObject)
	End If
End Function

Function checkJavaIsStart()
	strComputer = "."
	Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
	Set colProcessList = objWMIService.ExecQuery("Select * from Win32_Process Where Name = 'java.exe'")
	If colProcessList.Count>0 Then
		Set requestContent=HttpRequest(checkJavaIsOkUrl)
		code =requestContent.readystate
		If code=4 Then
			checkJavaIsStart=1
		Else
			checkJavaIsStart=2
		end If
	Else
		checkJavaIsStart=3
	End If
End Function

Function checkFlashIsStart(versionObject)
	version=versionObject.version
	strComputer = "."
	Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
	Set colProcessList = objWMIService.ExecQuery("Select * from Win32_Process Where Name = 'dmMovie.exe'")
	If colProcessList.Count>0 Then

		Set requestResult=HttpRequest(checkflashIsOkUrl)
		code =requestResult.readystate
		If requestResult.readystate=4 Then

			Set  resultObject=ParseJson(requestResult.responsetext)

			If resultObject.data="0" OR  resultObject.data="" Then
				Call showDailog("Flash self-test failed after update")
				Call SendFailRequestToServer("error",versionObject)
			Else
				Call SendSuccessRequestToServer("success",versionObject)
				Call writeContentFile(flashcurrentVersionPath,version)
			End If
		Else
			Call showDailog("Flash self-test failed after update,Java cannot be accessed")
			Call SendFailRequestToServer("error",versionObject)
		End If
	Else
		Call showDailog("flash does not start after update")
		Call SendFailRequestToServer("error",versionObject)
	End If
End Function


Function executeFlashUpdate(version)
	Call showDailog("flash execute update")
	'ws.run flashUpdateShell &" "& version
	Call executeShellFunction(flashUpdateShell &" "& version)
End Function

Function rollBack()

	Call killProcess
	
	Call showDailog("flash execute rollback")
	'ws.run flashRollbackShell
	Call executeShellFunction(flashRollbackShell)

	WScript.Sleep 20000

	If checkJavaIsStart=1 Then
		strComputer = "."
		Set objWMIService = GetObject("winmgmts:{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")
		Set colProcessList = objWMIService.ExecQuery("Select * from Win32_Process Where Name = 'dmMovie.exe'")
		If colProcessList.Count>0 Then

			Set requestResult=HttpRequest(checkflashIsOkUrl)
			code =requestResult.readystate
			If requestResult.readystate=4 Then
				Set  resultObject=ParseJson(requestResult.responsetext)
				If resultObject.data="0" OR  resultObject.data="" Then
					Call showDailog("Flash self-test failed after rollback")
				End If
			Else
				Call showDailog("Flash self-test failed after rollback,Java cannot be accessed")
			End If
		Else
			Call showDailog("flash does not start after rollback")
		End If
	ElseIf checkJavaIsStart=2 Then
		Call showDailog("Java cannot be accessed after rollback")
	ElseIf checkJavaIsStart=3 Then
		Call showDailog("Java does not start after rollback")
	End If

End Function

Function myRequestUrl(param,versionObject)
	url = operateRequestUrl & "?id=" & versionObject.id
	url = url & "&result=" & param
	url = url & "&machineNum=" & versionObject.machineNum
	url = url & "&type=1"
	myRequestUrl=url
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
	Call rollBack
End Function

Function SendSuccessRequestToServer(param,versionObject)
	url = myRequestUrl(param,versionObject)
	Set requestResult=HttpRequest(url)
	code =requestResult.readystate
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
