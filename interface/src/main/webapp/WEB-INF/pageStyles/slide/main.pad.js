    //采用DOMContentLoaded事件,并且在事件捕获阶段执行,无需等待图片加载完成
    //由于是基于android，无需考虑ie内核
    window.onload=function(){
        var as=document.getElementsByTagName("a");
        var length=as.length;
        var url="";
        var siteurl="http://lotusprize.com/hid";
        for(var i=0;i<length;i++){
            if(as[i].className=="videoslide"||as[i].getElementsByTagName("img").length!=0){

                as[i].onclick=function(){
                    if(this.className=="videoslide"){
                        var post_id=this.getAttribute("data-zy-post-id");
                        var media_id=this.getElementsByTagName("img")[0].getAttribute("data-zy-media-id");
                        url=siteurl+"/show_media/"+post_id+"/"+media_id;
                    }else{
                        url=this.getAttribute("href");
                    }

                    //跳转页面
                    window.location.href=url;
                    event.preventDefault();
                };
            }
        }
        
        var imgs=document.getElementsByTagName("img");
        var imgLength=imgs.length;
        for(var i=0;i<imgLength;i++){
        	if(imgs[i].parentNode.nodeName!="A"){
        		imgs[i].onclick=function(){
        			window.location.href=this.src;
        		}
        	
        	}
        }
        
    }

