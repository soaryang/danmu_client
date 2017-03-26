
Function showDailog(message)
    flg=false
    if flg=true then
        wShell.Popup message,3
    end if

End Function

Function ExecuteShellFunction(shellContent)
	ws.run shellContent,vbhide
End Function

Function Format_Time(s_Time, n_Flag)
	Dim y, m, d, h, mi, s
	Format_Time = ""
	If IsDate(s_Time) = False Then Exit Function
	y = cstr(year(s_Time))
	m = cstr(month(s_Time))
	If len(m) = 1 Then m = "0" & m
	d = cstr(day(s_Time))
	If len(d) = 1 Then d = "0" & d
	h = cstr(hour(s_Time))
	If len(h) = 1 Then h = "0" & h
	mi = cstr(minute(s_Time))
	If len(mi) = 1 Then mi = "0" & mi
	s = cstr(second(s_Time))
	If len(s) = 1 Then s = "0" & s
	Select Case n_Flag
		Case 1
			Format_Time = y  &"-"& m &"-"& d  &"-"& h  &"-"& mi &"-"& "00"
		Case 2
			Format_Time = y & "-" & m & "-" & d
		Case 3
			' hh:mm:ss
			Format_Time = h & ":" & mi & ":" & s
		Case 4
			Format_Time = y & "year" & m & "month" & d & "day"
		Case 5
			Format_Time = y & m & d

	End Select
End Function

Function ParseJson(strJson)
    Set window = html.parentWindow
    window.execScript "var json = " & strJson, "JScript"
    Set ParseJson = window.json
End Function

Function HttpRequest(url)
	On Error Resume Next
    http.open "GET", url, False
    http.send
    Set HttpRequest=http
End Function

Function setResultToFile(id,version,machineNum,status,requestCode,domainName,updateDate)
	Set file = fso.OpenTextFile(resultFilePath, 2)
	file.write("{")
	file.Write(Chr(34) & "id" & Chr(34)&":"&Chr(34)& id & Chr(34) & ",")
	file.Write(Chr(34) & "version" & Chr(34)&":"&Chr(34)& version & Chr(34) & ",")
	file.Write(Chr(34) & "machineNum" & Chr(34)&":"&Chr(34)& machineNum & Chr(34) & ",")
	file.Write(Chr(34) & "status" & Chr(34)&":"&Chr(34)& status & Chr(34) & ",")
	file.Write(Chr(34) & "code" & Chr(34)&":"&Chr(34)& requestCode & Chr(34) & ",")
	file.Write(Chr(34) & "updateDate" & Chr(34)&":"&Chr(34)& updateDate & Chr(34) & ",")
	file.Write(Chr(34) & "domainName" & Chr(34)&":"&Chr(34)& domainName & Chr(34))
	file.write("}")
	file.close
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

Function killProcess()
	ws.run "taskkill /F /IM java.exe"
	ws.run "taskkill /F /IM dmMovie.exe"
End Function

Function myRequestUrl(param,versionObject)
	url = operateRequestUrl & "?id=" & versionObject.id
	url = url & "&result=" & param
	url = url & "&machineNum=" & versionObject.machineNum
	url = url & "&type=0"
	myRequestUrl=url
End Function