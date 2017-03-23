{
"show": "r",
"expressionScale": 0.3,
"toPublicVideo":"1.flv",
"dsFaceIn":true,
"maxSize": 60,
"screen": 100,
"speed1": 0.7,
"bgColor": "0x000000",
"bgVisible": true,
"testServer": false,
"offY": 0,
"textOrder": false,
"topShow": false,
"localVideo": true,
"addBgJpg": false,
"bgJpgUrl": "localResoource/background/bg.jpg",
"setZ": 0,
"addRectWidth": 0,
"addRectHeight": 0,
"valueAbs": 10,
"camera": false,
"livePlayer": false,
"liveServer": null,
"liveStream": null,
"flightY":0,
"flightScale":1,
<#if partyResourceModelList??>
"partys":[
    <#list partyResourceModelList as partyResource>
    {"partyId":"${partyResource.party.id}",
    "name":"${partyResource.party.name}",
    <#if partyResource.party.movieAlias??>"movieAlias":"${partyResource.party.movieAlias}",</#if>
        <#if partyResource.bigExpressionList??>
        "expressions": [
            <#list partyResource.bigExpressionList as expression>
            {
            "url": "${expression.localFilePath}","id":"${expression.id}"
            }<#if expression_has_next>,</#if>
            </#list>
        ],
        </#if>
        <#if partyResource.specialImageList??>
        "specialEffects": [
            <#list partyResource.specialImageList as specialImage>
            {
            "url": "${specialImage.localFilePath}","id":"${specialImage.id}"
            }<#if specialImage_has_next>,</#if>
            </#list>
        ],
        </#if>
        <#if partyResource.specialVideoList??>
        "localVideoUrl": [
            <#list partyResource.specialVideoList as specialVideo>
            {
            "id":"${specialVideo.id}",
            "type": "click",
            "url": "${specialVideo.localFilePath}"
            }<#if specialVideo_has_next>,</#if>
            </#list>
        ]
        </#if>
        ,"timerDanmuUrl": [
        <#if partyResource.pathList??>
            <#list partyResource.pathList as path>
             {"url":"${path}"}<#if path_has_next>,</#if>
            </#list>
        </#if>
        ]
        <#if partyResource.adTimerDanmuPath??>,"adTimerDanmuUrl":"${partyResource.adTimerDanmuPath}"</#if>
    }<#if partyResource_has_next>,</#if>
    </#list>
</#if>
],
"adExpressions": [
    <#list adExpressionList as adExpression>
    {
    "url": "${adExpression.localFilePath}","id":"${adExpression.id}"
    }<#if adExpression_has_next>,</#if>
    </#list>
],
"adSpecialEffects": [
    <#list adSpecialEffectList as adSpecialEffects>
    {
    "url": "${adSpecialEffects.localFilePath}","id":"${adSpecialEffects.id}"
    }<#if adSpecialEffects_has_next>,</#if>
    </#list>
],
"adVideoUrl": [
    <#list adVideoUrlList as video>
    {
    "url": "${video.localFilePath}","id":"${video.id}","type": "click"
    }<#if video_has_next>,</#if>
    </#list>
]

}