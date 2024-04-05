package com.example.collegehunt

class chatmsg(val id:String, val text: String, val fromID:String, val toID:String, val timeStamp:Long)
{
    constructor():this("","","","",-1)

}