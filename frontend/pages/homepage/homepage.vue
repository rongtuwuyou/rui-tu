<template>
	<view>
		<view class="back" style="height: 100vh;width: 100vw; background-color:#F3F5FA ;"><!-- #171B26 -->
			
		</view>
		
		<!-- 左侧栏 -->
		<view class="left"> 
		
			<!-- 头部睿途logo -->
			<view class="lefthead"> 
				<img src="/static/logo.png" alt="" width='27%' />
				<img :src="modepic[[mode]].title" alt="" width='68%' height='18%' style="padding-top: 12px;padding-left: 6px;"/>
			</view>
			
			<!-- <button class="createSession" @click="createSession">+ 发起新对话</button> -->
			<el-button class="createSession" @click="createSession" color="#266FFF" :dark="isDark" plain>+ 发起新对话</el-button>
			
			<!-- 对话记录 -->
			<view class="diaHistory"> 
				<view class="hisTitle">
					<view style="margin-right: 60%;//160px;">对话记录</view>
					<img :src="modepic[[mode]].down" alt="" id='hisStatu' width='22px' @click="hisChange" style="cursor: pointer;"/>
				</view>
				<scroll-view class="historyList" id="historyList" scroll-y="true" @scrolltolower="getSessionMore">
					<view class="historyEle" v-for="(session,index) in sessionNames" @mouseover="hisOperate(index)" @mouseout='hisOperate2(index)'>
						<view class="hisText" @click="getDialogue(session.sessionId,index)" >
							<view class="hisWord" style="max-width:80%;white-space: nowrap;overflow-x: clip;text-overflow: ellipsis;">
								{{session.sessionName}}
							</view>
							<view class="hidden" :id="'statuIcon'+index">{{session.statuIcon}}</view>
						</view> 
						
						<view class="hidden" :id="'hisOperate'+index">
					<img  :src="modepic[[mode]].edit" alt="" width='16px' height='16px' style="margin-top: 4px;margin-right: 4px;cursor: pointer;" @click="hisRename(session.sessionId,index)" />
							<uni-icons  type="trash" size="16" color="#415058" style="margin-top: 4px;cursor: pointer;" @click="hisDel(session.sessionId,index)" ></uni-icons>
						</view>
					</view>
				</scroll-view>
			</view>
			 
			<!-- 最低处雕花 -->
			<!-- <img src="/static/back2.png" alt="" width='100%' style="position: absolute;top: 88%;//690px;"/> -->
			
			<!-- 用户信息区 -->
			<view class="user" @click="toPersonalCenter"> 
				<img :src="headUrl" alt="" width='48px' height='48px' style="border-radius: 360px;" />
				<view style="font-size: 24px;width: 10vw;: 48px;line-height: 48px;margin-left: 20px;white-space: nowrap;overflow-x: clip;text-overflow: ellipsis;">{{nickname}}</view>
			</view>
		</view>
		
		<!-- 右侧对话栏 -->
		<view class="right">
			
			<!-- 头部“成都”字样 -->
			<!-- <img src="/static/chengdu.png" alt="" height='90px' style="left: 65vw;//900px;position: absolute;top: 3vh;//30px;z-index: 0;"/> -->
			
			<!-- 头部对话抬头 -->
			<view class="righthead" @click="toAnaly">
				  对话  
					<span class="changeText">点击切换析图模式</span>
			</view>
			
			<!-- 新对话页面 -->
			<view class="firstQus" id="firstQus">
				<img :src="modepic[[mode]].firstQus" alt="" width="60%" />
		        <!-- <view style="font-weight: bold;margin-bottom: 8px;font-size: 20px;letter-spacing: 1px;">你可以问我：</view>
				<view>· 最近成都有什么新政策？</view>
				<view>· 最近成都那些路段比较拥堵？</view>
				<view style="font-weight: bold;margin-top: 10px;margin-bottom: 8px;font-size: 20px;letter-spacing: 1px;">你还可以让我识别一张图片：</view>
				<view>· 图中道路是否含有坑洼？</view>
				<view>· 图中道路是否清洁？</view>
				<view>· 图中是否发生了交通事故？</view>
				<view>· 图中是否含有违规占道情况？</view> -->
			</view>
			<!-- <img class="firstQus" id="firstQus" src="/static/firstQus.png" alt="" height='340px' /> -->
			
			<!-- 对话框 -->
			<view class="dialoguebox" >
				<scroll-view scroll-y="true"  class="dialogueList" id='dialogueList'  @scrolltoupper="getDiaMore" :scroll-top="scrollTop" >
					<view id='dialogue'>
						<view class="dialogue" v-for="(item,index) in dialogueLists" :key="index">
							<!-- 时间戳 -->
							<view class="time">
								{{index}}
							</view>
							<!-- 用户消息栏 -->
							<view class="userDia" >
								<img :src="headUrl" alt="" height='30px' width='30px' style="border-radius: 360px;" />
								<view class="Diabox" style="margin-left: 10px;">
									<view class="userContent">
										<img class="diaPic" :src="item.photoUrl" onerror="this.style.diaplay='none'" alt="" width='150vw'/>
										<view>{{item.contentUser}}</view>
									</view>
									<!-- 方案二：时间戳在消息框下方 -->
									<!-- <view class="usertime">
										{{index}}
									</view> -->
								</view>
							</view>
							<!-- 系统消息栏 -->
							<view class="aiDia" ><!-- v-if="item.contentAi != ''" -->
								<img src="/static/logohead.png" alt="" height='30px' width='30px'/>
								<view class="Diabox" style="margin-left: 10px;">
									<view class="aiContent" >
										<img :src="item.photoUrlAi" onerror="this.style.diaplay='none'" alt="" width='820vw' />
										<!-- {{item.contentAi}} -->
										<text v-html="item.htmlText"></text>
									</view>
									<view class="boxButtom">
										<view class="options">
											<img class="option" src="/static/copy.png" @click="copy(item.contentAi)" height='16px' width='16px' />
											<img class="option"  width='18px' height='18px' :src="item.handUp" alt="" @click="handUp(item)"/>
											<img class="option"  width='18px' height='18px' :src="item.handDown" alt="" @click="handDown(item)" />
											<!-- <uni-icons class="option" type="hand-up" size="18" color="#415058" @click="handUp()" ></uni-icons>
											<uni-icons class="option" type="hand-down" size="18" color="#415058"></uni-icons> -->
										</view>
										<!-- <view class="aitime">
											{{index}}
										</view> -->
									</view>
								</view>							
							</view>
						</view>
					</view>
					
				</scroll-view>
				
				<!-- 输入区域 -->
				<view class="inputcontent">
					<view class="photo" @click="Photo">
						<uni-icons  type="image-filled" size="30" color="#266FFF"></uni-icons>
					</view>	
					<view class="video" @click="Video">
						<img src="/static/video.png" alt="" width="34px" />
					</view>
					<button class="send" @click="sendQuestion">发送</button>
					<textarea  class="textarea" id="questionPut" cols="30" rows="10" v-model="QusForm.question" maxlength="500" placeholder="给睿途发消息" placeholder-style="letter-spacing:1px;color:#9A9A9A;"></textarea>
					<img class="picChose" :src="image_url" onerror="this.style.diaplay='none'" alt="" height='80px' width='120px' @mouseover='picDelicon' @mouseout='picDelicon2' /> 
					<img id='picDel' class='hidden' src="/static/delete.png" alt="" @click="picDel" width='20px' height='20px' />	
				</view>
				
				
			</view>
		</view>	
		
		<!-- 析图模式 -->
		<view class="analysis" id="analysis">
			<view class="anahead" @mouseover="anachangeText1" @mouseout="anachangeText2" @click="toDia" >
				  析图  
					<view class="hidden" id="anachangeText">点击切换对话模式</view>
			</view>
			<view class="AI" id="AI" style="width: 50vw;position: absolute;top: 45vh;left: 30vw;z-index: 1;"></view>
			<view class="ball" id="ball" style="width: 20vw;position: absolute;top: 12vh;left: 44vw;cursor: pointer;z-index: 1;" @mouseover="putAlert1" @mouseout="putAlert2" @click="Photo">
				<view class="putAlert" id="putAlert">
					点击放置图片
				</view>
				<img :src="image_url" alt=""  width="150vw" height="100vh" style="position: absolute;top: 35%;left: 24%;"/>
			</view>
			<view class="screen" id="screen" style="width: 35vw;position: absolute;top:10vh;left:65vw;z-index: 0;transform: scaleX(-1);"></view>
			<view class="screen2" id="screen2" style="width: 45vw;position: absolute;top:14vh;z-index: 0;left:2%;transform: scaleX(-1);"></view>
			<view class="anaButton" style="left: 41%;border: solid #64FD87 2px;	color: #64FD87;z-index: 1" @click="picDel">删除	</view>
			<view class="anaButton" style="left: 62%;border: solid #10F2FF 2px;	color: #10F2FF;z-index: 1" @click="sendQuestion">分析	</view>
			<view class="anaAI" >
				<view v-for="(item,index) in dialogueLists">
					<img class="anaAIpic" :src="item.photoUrlAi" alt="" />
					<view class="anaAIcon" v-html="item.htmlText"></view>
					<view class="rebegin" @click="rebegin()">
						点击再分析一张 >>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 右部设置栏 -->
		<view class="setting">
			<img src="/static/mode.png" alt="" width="18px" style="cursor: pointer;" @click="changeMode"/>
			<view style="margin-top: 10px;"><uni-icons type="person-filled" size="25" color="#9A9A9A" style="cursor: pointer;" @click="toPersonalCenter"></uni-icons></view>
			<view style="margin-top: 10px;"><uni-icons type="gear-filled" size="25" color="#9A9A9A"></uni-icons></view>
			<view style="margin-top: 10px;cursor: pointer;" @click="toDeclare" ><uni-icons type="more-filled" size="25" color="#9A9A9A"></uni-icons></view>
			
		</view>
	</view>
</template>

<script>
import { gsap} from 'gsap';
import lottie from 'lottie-web';
import { vModelText } from 'vue';
import showdown from "showdown";

	export default {
		data() {
			return {
				token:'',
				QusForm:{
					question:'',
					image_url:null,//不拼接
					token:''
				},//detct接口data表单
				photoUrl:null,//上传对话记录所需，不拼接（用户）
				photoUrlAi:null,//上传对话所需，不拼接（AI）
				image_url:null,//页面展示所需，拼接
				compressed:null,//压缩后的
				sessionNames:[
					
				],
				dialogueLists:{
					// dialogueLists[[dict.createTime]]={
								// 	contentUser:dict.contentUser,
								// 	contentAi:dict.contentAi,
								// 	photoUrl:dict.photoUrl,
								//  likeStatus:2,
								//  handUp:'static/hand_up.png',
								//  handDown:'static/down_fill.png'
								// }
					
				},
				sessionId:'',
				nickname:'游客',
				headUrl:'/static/head.png',
				sessionNewName:'',
				sessionQuery:{
					pageSize:10,
					pageNum:1
				},
				diaPageNum:1,
				scrollTop:0,
				abortToBot: false,
				heightRecord:null,
				timer:null,
				handleAi:false,
				ai:null,
				ball:null,
				pageStatu:0,//0为对话，1为析图
				htmlText:'',
				mode:0,
				modepic:[
					{
						title:"/static/title_black.png",
						edit:"/static/edit_black.png",
						up:"/static/up_black.png",
						down:"/static/down_black.png",
						firstQus:"/static/firstQus.png"
					},
					{
						title:"/static/title.png",
						edit:"/static/edit.png",
						up:"/static/up.png",
						down:"/static/down.png",
						firstQus:"/static/firstQus_black.png"
					}
				]
			};
		},
		methods:{
			getToken(){
				this.token=uni.getStorageSync('access_token')
				this.QusForm.token=this.token
				console.log(this.token)
			},
			getUser(){
				let nickname=uni.getStorageSync('nickname')
				if(nickname){
					this.nickname=nickname
				}
				let head=uni.getStorageSync('headUrl')
				if(head=='/static/head.png'){
					this.headUrl=head
				}
				else if(head){
					this.headUrl='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+head
				}
			},
			getSessionFirst(){
				this.sessionNames=[]
				this.sessionQuery.pageNum=1
				this.getsessionNames()
			},
			getsessionNames(){
				uni.request({
					url:getApp().globalData.domain+'/session/get_session_list',
					method:'GET',
					header:{
						token:this.token
					},
					data:this.sessionQuery,
					success: (res) => {
						if(res.statusCode==200 && res.data.code==1){
							console.log(res)
							var dataList=res.data.data.dataList
							for(var i in dataList){
								var session=dataList[i]
								if(session.sessionName==''){
									session.sessionName='图片'
								}
								if(session.status){
									this.sessionNames.push({
										sessionId:session.id,
										sessionName:session.sessionName,
										pageStatu:session.status,
										statuIcon:"析图"
									})
								}
								else{
									this.sessionNames.push({
										sessionId:session.id,
										sessionName:session.sessionName,
										pageStatu:session.status,
										statuIcon:"对话"
									})
								}
								
								
							}
						}
						else if(res.statusCode==401){
							localStorage.removeItem('access_token')
							localStorage.removeItem('phone')
							localStorage.removeItem('username')
							localStorage.removeItem('nickname')
							localStorage.removeItem('identity')
							localStorage.removeItem('headUrl')
							this.getToken()
							this.getUser()
						}
					},
					fail() {
						uni.showToast({
							icon:'none',
							title:'网络错误'
						})
					}
				})
			},
			getSessionMore(){
				this.sessionQuery.pageNum++
				this.getsessionNames()	
			},
			createSession(){
				this.sessionId=''
				this.dialogueLists={}
				if(!this.pageStatu){
					document.getElementById('firstQus').className='firstQus'
					document.getElementById('hisChose').id='historyEle'
				}
			},
			getDialogue(sessionId,index){
				this.abortToBot=false
				this.sessionId=sessionId
				this.diaPageNum=1
				 uni.request({
				 	url:getApp().globalData.domain+'/session/get_session_detail',
					method:'GET',
					header:{
						token:this.token
					},
					data:{
						sessionId:sessionId,
						pageSize:20,
						pageNum:this.diaPageNum
					},
					success: (res) => {
						if(this.sessionNames[index].pageStatu){
							this.toAnaly()
						}
						console.log(res)
						this.dialogueLists={}
						
						//对话页面进行高亮处理
						if(!this.pageStatu){
							document.getElementById('firstQus').className='hidden'
							let txt=document.getElementsByClassName('historyEle')[index]
							if(document.getElementById('hisChose')){
								document.getElementById('hisChose').id='historyEle'
							}
							txt.id='hisChose'
						}
						
						if(res.statusCode==200 && res.data.code==1){
							var dataList=res.data.data.dataList
							dataList.reverse()
							for(var i in dataList){
								var dict=dataList[i]
								var stanUrl=dict.photoUrl
								var AiUrl=dict.photoUrlAi
								if(stanUrl.length){
									for(var i in stanUrl){
										stanUrl[i]='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+stanUrl[i]
									}
								}
								else{
									stanUrl=null
								}
								if(AiUrl.length){
									for(var i in AiUrl){
										AiUrl[i]='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+AiUrl[i]
									}
								}
								else{
									AiUrl=null
								}
								this.dialogueLists[[dict.createTime]]={
									contentUser:dict.contentUser,
									contentAi:dict.contentAi,
									photoUrl:stanUrl,
									photoUrlAi:AiUrl,
									likeStatus:dict.likeStatus,
									id:dict.id
								}
								let diaEle=this.dialogueLists[[dict.createTime]]
								if(diaEle.likeStatus==0){
									diaEle['handUp']='/static/hand_up.png'
									diaEle['handDown']='/static/hand_down.png'
								}
								else if(diaEle.likeStatus==1){
									diaEle['handUp']='/static/up_fill.png'
									diaEle['handDown']='/static/hand_down.png'
								}
								else if(diaEle.likeStatus==2){
									diaEle['handUp']='/static/hand_up.png'
									diaEle['handDown']='/static/down_fill.png'
								}
								console.log("现在的对话表",this.dialogueLists)
								let converter = new showdown.Converter();
								// 显示表格
								converter.setOption("tables",true);
								diaEle.htmlText = converter.makeHtml(diaEle['contentAi']); 
								if(this.sessionNames[index].pageStatu){
									this.showAI()
									console.log("对话表",this.dialogueLists)
								}
							}
						}
					},
					fail() {
						uni.showToast({
							icon:'none',
							title:'网络错误'
						})
					}
				 })
				 
			},
			getDiaMore(){
				this.abortToBot=true
				this.diaPageNum++
				uni.request({
					url:getApp().globalData.domain+'/session/get_session_detail',
					method:'GET',
					header:{
					token:this.token
					},
					data:{
					sessionId:this.sessionId,
					pageSize:20,
					pageNum:this.diaPageNum
					},
					success: (res) => {
					console.log(res)
					if(res.statusCode==200 && res.data.code==1){
							var dataList=res.data.data.dataList
							// dataList.reverse()
							for(var i in dataList){
							var dict=dataList[i]
							var stanUrl=dict.photoUrl
							var AiUrl=dict.photoUrlAi
									if(stanUrl.length){
										for(var i in stanUrl){
											stanUrl[i]='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+stanUrl[i]
										}
									}
									else{
										stanUrl=null
									}
							if(AiUrl.length){
								for(var i in AiUrl){
									AiUrl[i]='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+AiUrl[i]
								}
							}
							else{
								AiUrl=null
							}
							this.dialogueLists={
								[dict.createTime]:{
									contentUser:dict.contentUser,
									contentAi:dict.contentAi,
									photoUrl:stanUrl,
									photoUrlAi:AiUrl,
									likeStatus:dict.likeStatus,
									id:dict.id
								},...this.dialogueLists
							}
							let diaEle=this.dialogueLists[[dict.createTime]]
							if(diaEle.likeStatus==0){
								diaEle['handUp']='/static/hand_up.png'
								diaEle['handDown']='/static/hand_down.png'
							}
							else if(diaEle.likeStatus==1){
								diaEle['handUp']='/static/up_fill.png'
								diaEle['handDown']='/static/hand_down.png'
							}
							else if(diaEle.likeStatus==1){
								diaEle['handUp']='/static/hand_up.png'
								diaEle['handDown']='/static/down_fill.png'
							}
							}
						}
					},
									fail() {
										uni.showToast({
											icon:'none',
											title:'网络错误'
										})
									}
				})	
			},
			hisChange(){
				let txt=document.getElementById('historyList')
				let statu=document.getElementById('hisStatu')
				if (txt.className=='historyList'){
					txt.className='hidden'
					statu.src='static/up_black.png'
				}
				else{
					txt.className='historyList'
					statu.src='/static/down_black.png'
				}
			},
			hisOperate(index){
				let dom=document.getElementsByClassName('historyEle')[index]
				let txt=dom.getElementsByClassName('hidden')
				for(var n=0;n<txt.length;){
					if(txt[n].id=='hisOperate'+index.toString()){
						txt[n].className='hisOperate'
						
					}
					else if(txt[n].id=='statuIcon'+index.toString()){
						txt[n].className='statuIcon'
					}
				}
			},
			hisOperate2(index){
				let dom=document.getElementsByClassName('historyEle')[index]
				let txt=dom.getElementsByClassName('hisOperate')
				let txt2=dom.getElementsByClassName('statuIcon')
				for(var i=0;i<txt.length;i++){
					txt[i].className='hidden'
				}
				for(var m=0;m<txt2.length;m++){
					txt2[m].className='hidden'
				}
				
			},
			hisRename(sessionId,index){
				let txt=document.getElementsByClassName('hisWord')[index]				
				this.sessionNewName=txt.innerHTML				
				const inputEle=document.createElement('input')
				inputEle.type='text'
				inputEle.value=this.sessionNewName
				// inputEle.vModelText=this.sessionNewName
				inputEle.className='nameInput'
				txt.innerHTML=''
				txt.appendChild(inputEle)
				inputEle.focus()
				inputEle.addEventListener('keyup',(e)=>{
					if(e.key=='Enter'){
						 const newContent = inputEle.value.trim();
						    if (newContent) {
						        this.sessionNewName = newContent; 
						        txt.textContent = newContent; 
						    } else {
						        txt.textContent = this.sessionNewName; 
						    }
						uni.request({
							url:getApp().globalData.domain+'/session/update_session_name',
							method:'PUT',
							header:{
								token:this.token
							},
							data:{
								id:sessionId,
								sessionName:this.sessionNewName
							},
							success: (res) => {
								console.log('更改成功')
							},
							fail() {
								uni.showToast({
								icon:'none',
								title:'网络错误'
						})
							}
						})
					}
					else if(e.key === 'Escape'){
						console.log(this.sessionNewName)
						txt.textContent = this.sessionNewName; 
					}
				})
				inputEle.addEventListener('blur', ()=>{
				        txt.textContent = this.sessionNewName; 
				    });
			},
			hisDel(sessionId){
				let r=confirm('确定删除该对话？')
				if(r){
					uni.request({
						url:getApp().globalData.domain+'/session/delete_session/'+sessionId,
						method:'DELETE',
						header:{
							token:this.token
						},
						success: (res) => {
							if(res.statusCode==200 && res.data.code){
								this.getSessionFirst();
							}
							else{
								uni.showToast({
									icon:'none',
									title:rea.data.msg
								})
							}
						},
						fail() {
							uni.showToast({
								icon:'none',
								title:'网络错误'
							})
						}
					})
				}
			},
			toAnaly(){
				this.ball.setSpeed(1)
				const t1=gsap.timeline();
				t1.to([".right",".createSession",".diaHistory"],{opacity:0,ease: 'expo.out',duration: 1,visibility: "hidden"})
				  .to(".analysis",{opacity:1,ease: 'expo.in',duration: 1,visibility: "visible"},"-=1")
				  .to([".screen",".ball",".screen2"],{opacity:1,ease: 'expo.in',duration: 1,visibility:"visible"},"-=1")
				  .to(".AI",{x:0,duration: 1},"-=1")
				  .to(".anaAI",{opacity:0,ease: 'expo.in',duration: 1,visibility:"hidden"},"-=1")//[".anaAIcon",".rebegin"]
				
				this.pageStatu=1
			},
			copy(content){
				var aux = document.createElement("input"); 
				    aux.setAttribute("value", content); 
				    document.body.appendChild(aux); 
				    aux.select();
				    document.execCommand("copy"); 
				    document.body.removeChild(aux);
				    uni.showToast({
							icon:'none',
							title:'复制成功'
						})
			},
			Photo(){
				if(!this.token){
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
				else{
					uni.chooseImage({
						// count:9,
						sourceType:['album'],
						sizeType:'compressed',
						success:function(res){
							var photoUrl=res.tempFilePaths[0]
							console.log(photoUrl)
							
							//compressed函数
							const img = new Image();
							img.src = photoUrl;
							img.onload = () => {
							  const canvas = document.createElement('canvas');
							  const ctx = canvas.getContext('2d');
										
							  // 设置 Canvas 尺寸
							  const maxWidth = 800; // 最大宽度
							  const maxHeight = 800; // 最大高度
							  let width = img.width;
							  let height = img.height;
										
							  if (width > height) {
							    if (width > maxWidth) {
							      height *= maxWidth / width;
							      width = maxWidth;
							    }
							  } else {
							    if (height > maxHeight) {
							      width *= maxHeight / height;
							      height = maxHeight;
							    }
							  }
										
							  canvas.width = width;
							  canvas.height = height;
										
							  // 绘制图片到 Canvas
							  ctx.drawImage(img, 0, 0, width, height);
										
							  // 将 Canvas 转换为 Blob
							  canvas.toBlob(
							    (blob) => {
									const fileName = `compressed_${Date.now()}.jpg`;
									
							      const compressedFile = new File([blob], fileName, {
							        type: 'image/jpeg',
							        lastModified: Date.now(),
							      });
							      console.log('压缩后的文件', compressedFile);
												console.log(compressedFile.size)
												
												const compressedImageUrl = URL.createObjectURL(compressedFile);
												console.log("在子函数中",compressedImageUrl)
												
												const reader = new FileReader();
												 
												reader.onload = function(event) {
												    const arrayBuffer = event.target.result;
												    const byteArray = new Uint8Array(arrayBuffer);
												    console.log(byteArray);  // 打印出 Blob 的二进制数据
												};
												 
												reader.readAsArrayBuffer(blob);
												
												console.log("blob的信息",blob.type)
												
												
												uni.uploadFile({
													url:'https://www.rongtuwuyou.com:8083'+'/object/upload-io',
													file:compressedFile, 
													fileType: 'image', 
													name:"file",
													header:{
														token:this.token
													},
													success: (res) => {
														var res_dict=JSON.parse(res.data)
														this.photoUrl=[]
														this.image_url='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+res_dict.data.url
														this.QusForm.image_url=res_dict.data.url
														this.photoUrl.push(res_dict.data.url)
														console.log("上传的图片",photoUrl)
														if(this.pageStatu==1){
															this.sessionId=''
																gsap.to(".anaButton",{
																	duration:3,
																	opacity:1,
																	visibility:"visible"									
																})
														}
													},
													fail() {
														uni.showToast({
													icon:'none',
													title:'网络错误'
														})
													}
												})
							    },
							    'image/jpeg',
							    0.5 // 压缩质量
							  );
							};
							
						}.bind(this),
						fail() {
							uni.showToast({
								icon:'none',
								title:'网络错误'
							})
						}
					})
				}
			},
			picDel(){
				this.QusForm.image_url=null
				this.image_url=null
				if(this.pageStatu==1){
					gsap.to('.anaButton',{
						duration:1,
						opacity:0,
						visibility:"hidden"
					})
				}
			},
			picDelicon(){
				document.getElementById('picDel').className='picDel'
			},
			picDelicon2(){
				document.getElementById('picDel').className='hidden'
			},
			showAI(){
				const t4=gsap.timeline();
				t4.to([".ball",".anaButton",".screen",".screen2"],{opacity:0,ease: 'expo.out',duration: 1,visibility:"hidden"})
				  .to(".AI",{x:-350,duration: 1},"-=1")
				  .to(".anaAI",{opacity:1,ease: 'expo.in',duration: 1,visibility:"visible"},"-=1")//
			},
			rebegin(){
				this.ball.setSpeed(1)
				const t1=gsap.timeline();
				t1.to([".screen2",".screen",".ball"],{opacity:1,ease: 'expo.in',duration: 1,visibility:"visible"})
				  .to(".AI",{x:0,duration: 1},"-=1")
				  .to(".anaAI",{opacity:0,ease: 'expo.in',duration: 1,visibility:"hidden"},"-=1")//[".anaAIcon",".rebegin"]
				this.createSession()
			},
			sendQuestion(){
				if(!this.token){
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
				else{
					if(!this.handleAi){
						this.handleAi=true
						this.abortToBot=false
						if(! (this.QusForm.question || this.QusForm.image_url)){
							uni.showToast({
									icon:'none',
									title:'输入不能为空'
								})
						}
						else{
							if(this.pageStatu==1){
								this.ai.setSpeed(5)
								this.ai.play()
								this.ball.setSpeed(5)
							}
							document.getElementById('firstQus').className='hidden'
							const time= new Date()
							var creatTime=time.getFullYear()+'-'+('0'+(time.getMonth()+1)).slice(-2)+'-'+('0'+time.getDate()).slice(-2)+' '+('0'+time.getHours()).slice(-2)+':'+('0'+time.getMinutes()).slice(-2)+':'+('0'+time.getSeconds()).slice(-2)
							this.dialogueLists[[creatTime]]={}
							var dia=this.dialogueLists[[creatTime]]
							dia['contentUser']=this.QusForm.question
							dia['htmlText']='正在思考中 ...'
							dia['photoUrl']=[]
							for (var i in this.photoUrl){
								dia['photoUrl'].push('https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+this.photoUrl[i])
							}
							if(dia['photoUrl'].length==0){
								dia['photoUrl']=null
							}
							console.log(this.QusForm)
							
							if((!this.QusForm.question) || this.QusForm.question==''){
								uni.request({
									url:'https://www.rongtuwuyou.com/ml-api/picture_analyse',
									method:'POST',
									header:{
										token:this.token
									},
									data:{
										image_url:this.QusForm.image_url
									},
									success: (res) => {
										console.log("返回的res",res)
										if(res.statusCode==200 && res.data.polished_answer){
											// if(res.data.detections.image_url){
											// 	this.photoUrlAi=[]
											// 	this.photoUrlAi.push(res.data.image_url)
											// 	dia['photoUrlAi']=[]
											// 	for(var i in this.photoUrlAi){
											// 		dia['photoUrlAi'].push('https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+this.photoUrlAi[i])
											// 	}
											// }
											// else{
												dia['photoUrlAi']=null
												this.photoUrlAi=null
											// }
											if(this.pageStatu==1){
												this.showAI()
												if(!this.photoUrlAi){
												dia['photoUrlAi']=dia['photoUrl']
												this.photoUrlAi=this.photoUrl
												}
											}
											var results=res.data.polished_answer
											var results=JSON.parse(res.data.polished_answer)
											results=results["choices"][0]['message']['content']
											this.verDis(results,creatTime)
										}
										// 以下是都要恢复的
										else{
											if(res.data.error){
												if(this.pageStatu==1){
													this.showAI()
													if(!this.photoUrlAi){
													dia['photoUrlAi']=dia['photoUrl']
													this.photoUrlAi=this.photoUrl
													}
												}
												dia['contentAi']=res.data.error
												dia['htmlText']=res.data.error
												this.verDis(res.data.error,creatTime)
											}
											else{
												if(this.pageStatu==1){
													this.showAI()
													if(!this.photoUrlAi){
													dia['photoUrlAi']=dia['photoUrl']
													this.photoUrlAi=this.photoUrl
													}
												}
												dia['contentAi']='服务器繁忙，请稍后再试。'
												dia['htmlText']='服务器繁忙，请稍后再试。'
												this.handleAi=false
												this.saveDia(dia['contentUser'],dia['contentAi'],creatTime)
											}
										}
									},
									fail: () => {
										if(this.pageStatu==1){
											this.showAI()
											if(!this.photoUrlAi){
											dia['photoUrlAi']=dia['photoUrl']
											this.photoUrlAi=this.photoUrl
											}
										}
										dia['contentAi']='服务器繁忙，请稍后再试。'
										dia['htmlText']='服务器繁忙，请稍后再试。'
										this.handleAi=false
										this.saveDia(dia['contentUser'],dia['contentAi'],creatTime)
									}
								})
							}
							
							
							
							
							
							// 对话
							else{
								uni.request({
									url:'https://www.rongtuwuyou.com/ml-api/detect',
									method:'POST',
									data:this.QusForm,
									success: (res) => {
										console.log("返回的res",res)
										// if(res.statusCode==200 && res.data.polished_answer){
											// if(res.data.detections.image_url){
											// 	this.photoUrlAi=[]
											// 	this.photoUrlAi.push(res.data.image_url)
											// 	dia['photoUrlAi']=[]
											// 	for(var i in this.photoUrlAi){
											// 		dia['photoUrlAi'].push('https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+this.photoUrlAi[i])
											// 	}
											// }
											// else{
												dia['photoUrlAi']='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+"daowuyou/2025/4/11/userId-8/678e40b9-b532-4f4b-9312-26e7c64be6d48.jpg"
												this.photoUrlAi='daowuyou/2025/4/11/userId-8/678e40b9-b532-4f4b-9312-26e7c64be6d48.jpg'
											// }
											if(this.pageStatu==1){
												this.showAI()
												if(!this.photoUrlAi){
												dia['photoUrlAi']=dia['photoUrl']
												this.photoUrlAi=this.photoUrl
												}
											}
											// var results=res.data.polished_answer
											// var results=JSON.parse(res.data.polished_answer)
											// results=results["choices"][0]['message']['content']
											var results="**坑槽的位置**：在图片中，路面中央存在明显的破损区域，形成多个坑槽。这些坑槽主要集中在车道的中间部分。\n\n**坑槽的深度和宽度**：从图片上看，坑槽的宽度大约为30-50厘米，深度估计在5-10厘米之间。\n\n**评估是否影响交通流畅性或存在安全隐患**：这些坑槽的存在严重影响了道路的平整度，可能导致车辆颠簸，甚至对车辆轮胎造成损害。特别是在夜间或雨天，驾驶员可能无法及时发现坑槽，存在较大的安全隐患。\n\n**处理建议**：坑槽位于车道的中央部分，靠近人行道边缘。建议尽快进行修复，以确保行车安全。"
											this.verDis(results,creatTime)
										// }
										// else{
										// 	if(res.data.error){
										// 		if(this.pageStatu==1){
										// 			this.showAI()
										// 			if(!this.photoUrlAi){
										// 			dia['photoUrlAi']=dia['photoUrl']
										// 			this.photoUrlAi=this.photoUrl
										// 			}
										// 		}
										// 		dia['contentAi']=res.data.error
										// 		dia['htmlText']=res.data.error
										// 		this.verDis(res.data.error,creatTime)
										// 	}
										// 	else{
										// 		if(this.pageStatu==1){
										// 			this.showAI()
										// 			if(!this.photoUrlAi){
										// 			dia['photoUrlAi']=dia['photoUrl']
										// 			this.photoUrlAi=this.photoUrl
										// 			}
										// 		}
										// 		dia['contentAi']='服务器繁忙，请稍后再试。'
										// 		dia['htmlText']='服务器繁忙，请稍后再试。'
										// 		this.handleAi=false
										// 		this.saveDia(dia['contentUser'],dia['contentAi'],creatTime)
										// 	}
										// }
									},
									fail: () => {
										if(this.pageStatu==1){
											this.showAI()
											if(!this.photoUrlAi){
											dia['photoUrlAi']=dia['photoUrl']
											this.photoUrlAi=this.photoUrl
											}
										}
										dia['contentAi']='服务器繁忙，请稍后再试。'
										dia['htmlText']='服务器繁忙，请稍后再试。'
										this.handleAi=false
										this.saveDia(dia['contentUser'],dia['contentAi'],creatTime)
									}
								})
							}
							
							dia['handUp']='/static/hand_up.png'
							dia['handDown']='/static/hand_down.png'
							dia['likeStatus']=0
							this.QusForm.question=''
							this.QusForm.image_url=null
							this.image_url=null
						}
					}
					else{
						uni.showToast({
							icon:'none',
							title:'请等回答结束后再发送'
						})
					}

				}
			},
			verDis(a,creatTime){
				var len=0
				this.dialogueLists[[creatTime]]['contentAi']=''
				var converter = new showdown.Converter();
				// 显示表格
				converter.setOption("tables",true);
				this.timer=setInterval(()=>{
					if(len<a.length){
						this.dialogueLists[[creatTime]]['contentAi']=this.dialogueLists[[creatTime]]['contentAi']+a[len]
						this.dialogueLists[[creatTime]]['htmlText'] = converter.makeHtml(this.dialogueLists[[creatTime]]['contentAi']); 
						len++
					}else{
						clearInterval(this.timer)
						// this.saveDia(this.dialogueLists[[creatTime]]['contentUser'],this.dialogueLists[[creatTime]]['contentAi'],creatTime)
						this.handleAi=false
					}
					console.log(this.dialogueLists[[creatTime]]['contentAi'])
				},30)
				
			},
			saveDia(user,ai,creatTime){
				// let data={
				// 		sessionId:this.sessionId,
				// 		contentUser:user,
				// 		contentAi:ai,
				// 		photoUrl:this.photoUrl,
				// 		creatTime:creatTime,
				// 		status:this.pageStatu
				// 	}
				// console.log(data)
				if(this.sessionId){
					uni.request({
						url:getApp().globalData.domain+'/session/create_dialogue',
						method:'POST',
						header:{
							token:this.token
						},
						data:{
							sessionId:this.sessionId,
							contentUser:user,
							contentAi:ai,
							photoUrl:this.photoUrl,
							photoUrlAi:this.photoUrlAi,
							creatTime:creatTime,
							status:this.pageStatu
						},
						success: (res) => {
							if(res.statusCode==200 && res.data.code==1){
								console.log('对话成功')
							}
							else{
								uni.showToast({
							icon:'none',
							title:res.data.msg
						})
							}
						},
						fail() {
							uni.showToast({
							icon:'none',
							title:'网络错误'
						})
						}
					})
				}
				else{
					uni.request({
						url:getApp().globalData.domain+'/session/create_session',
						method:'POST',
						header:{
							token:this.token
						},
						data:{
							contentUser:user,
							contentAi:ai,
							photoUrl:this.photoUrl,
							photoUrlAi:this.photoUrlAi,
							creatTime:creatTime,
							status:this.pageStatu
						},
						success: (res) => {
							if(res.statusCode==200 && res.data.code==1){
								console.log('对话成功')
								this.sessionId=res.data.data.id
								this.getSessionFirst();
							}
							else{
								uni.showToast({
							icon:'none',
							title:res.data.msg
						})
							}
						},
						fail() {
							uni.showToast({
							icon:'none',
							title:'网络错误'
						})
						}
					})
				}
				this.photoUrl=null
				this.photoUrlAi=null
			},
			handUp(item){
				if(item.likeStatus==1){
					item.likeStatus=0
					item.handUp='/static/hand_up.png'
					uni.request({
						url:getApp().globalData.domain+'/session/dialogue/update_like_status',
						method:'PUT',
						header:{
							token:this.token
						},
						data:{
							id:item.id,
							likeStatus:0
						}
					})
				}
				else{
					item.likeStatus=1
					item.handUp='/static/up_fill.png'
					item.handDown='/static/hand_down.png'
					uni.request({
						url:getApp().globalData.domain+'/session/dialogue/update_like_status',
						method:'PUT',
						header:{
							token:this.token
						},
						data:{
							id:item.id,
							likeStatus:1
						}
					})
					
				}
			},
			handDown(item){
				if(item.likeStatus==2){
					item.likeStatus=0
					item.handDown='/static/hand_down.png'
					uni.request({
						url:getApp().globalData.domain+'/session/dialogue/update_like_status',
						method:'PUT',
						header:{
							token:this.token
						},
						data:{
							id:item.id,
							likeStatus:0
						}
					})
				}
				else{
					item.likeStatus=2
					item.handUp='/static/hand_up.png'
					item.handDown='/static/down_fill.png'
					uni.request({
						url:getApp().globalData.domain+'/session/dialogue/update_like_status',
						method:'PUT',
						header:{
							token:this.token
						},
						data:{
							id:item.id,
							likeStatus:2
						}
					})
				}
			},
			observeDiaChanges(){
				var txt=document.getElementById('dialogue')
				  const observer = new MutationObserver((mutationsList) => {
				        for (const mutation of mutationsList) {
				          if (mutation.type === 'childList') {
				            console.log('子节点发生变化:', mutation);
							this.diaToBottom()
				          } else if (mutation.type === 'attributes') {
				            console.log('属性发生变化:', mutation);
							this.diaToBottom()
				          }
				        }
				      });
				const config = {
				        attributes: true, 
				        childList: true, 
				        subtree: true,
				      };
				const resizeObserver = new ResizeObserver((entries) => {
				    for (let entry of entries) {
				        const height = entry.contentRect.height;
				        console.log('元素高度发生变化:', height);
						this.diaToBottom()
				    }
				});
				resizeObserver.observe(txt)
				observer.observe(txt,config);
				this.observer = observer;
			},
			diaToBottom(){
				let txt=document.getElementsByClassName('dialogueList')[0]
				console.log(txt.scrollHeight)
				 uni.createSelectorQuery()
				        .select('#dialogue') // 选择内容区域的标识
				        .boundingClientRect((rect) => {
				          if (rect) {
				            console.log('内容区域总高度:', rect.height);
							if(! this.abortToBot){
								this.scrollTop=rect.height
								this.heightRecord=rect.height
							}
							else{
								this.scrollTop=rect.height-this.heightRecord
							}
				          }
				        })
				        .exec();
			},
			toPersonalCenter(){
				if(this.token){
					uni.navigateTo({
						url:'/pages/personalCenter/personalCenter'
					})
				}
				else{
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
			},
			putAlert1(){
				document.getElementById('putAlert').className="putAlert"
			},
			putAlert2(){
				document.getElementById('putAlert').className="hidden"
			},
			anachangeText1(){
				document.getElementById("anachangeText").className="anachangeText"
			},
			anachangeText2(){
				document.getElementById("anachangeText").className="hidden"
			},
			toDia(){
				const t2=gsap.timeline();
				t2.to(".analysis",{opacity:0,ease: 'expo.out',duration: 1,visibility: "hidden"})
				  .to([".right",".createSession",".diaHistory"],{opacity:1,ease: 'expo.in',duration: 1,visibility: "visible"},"-=1")
				
				this.pageStatu=0
				this.createSession()
			},
			toWhite(){
				let t5=gsap.timeline()
				t5.to(".back",{backgroundColor:"#F3F5FA "})
				  .to([".diaHistory",".user",".userContent",".righthead",".textarea"],{color:"#415058"},"-=1")
				  .to(".right",{border:"none",boxShadow:" 0px 0px 6px 0px  #41505860",backgroundColor:"#FAFBFF"},"-=1")
				  .to(".aiContent",{background:"linear-gradient(-45deg,#ABFF8420,#266FFF20)"},"-=1")
				  .to(".inputcontent",{border:" solid 1px #266FFF"},"-=1")
				  .to(".send",{color:"#FFF"},"-=1")
			},
			toBlack(){
				let t5=gsap.timeline()
				t5.to(".back",{backgroundColor:"#171B26 "})
				  .to([".diaHistory",".user",".textarea"],{color:"#DADDE5"},"-=1")
				  .to(".right",{border:" solid 1px #07D4E1",boxShadow:" 0px 0px 6px 0px  #377F7F",backgroundColor:"#FAFBFF00"},"-=1")
				  .to(".userContent",{color:"#9A9A9A"},"-=1")
				  .to(".aiContent",{background:"linear-gradient(-45deg,#ABFF84,#266FFF)"},"-=1")
				  .to(".righthead",{color:"#266FFF"},"-=1")
				  .to(".inputcontent",{border:"none"},"-=1")
				  .to(".send",{color:"#303030"},"-=1")
			},
			getMode(){
				this.mode=localStorage.getItem("mode")
				if(this.mode==null){
					this.mode=0
					localStorage.setItem("mode")
				}
				if(!this.mode){
					this.toWhite()
				}
				else{
					this.toBlack()
				}
			},
			changeMode(){
				if(!this.mode){
					this.mode=1
					localStorage.setItem("mode",1)
					this.toBlack()
				}
				else{
					this.mode=0
					localStorage.setItem("mode",0)
					this.toWhite()
				}
			},
			toDeclare(){
				uni.navigateTo({
					url:'/pages/declare/declare'
				})
			}
		},
		created(){
			this.getToken();
			this.getUser();
			this.getSessionFirst();
			this.getMode();
		},
		mounted() {
			this.observeDiaChanges();
			this.ai=lottie.loadAnimation({
			      container: document.getElementById('AI'), // 动画容器
			      renderer: 'svg', // 渲染方式，可以是 'svg'、'canvas' 或 'html'
			      loop: false, // 是否循环播放
			      autoplay: false, // 是否自动播放
			      path: '/static/svgJson/AI.json' // 动画 JSON 文件的路径
			});
			this.ball=lottie.loadAnimation({
			      container: document.getElementById('ball'), // 动画容器
			      renderer: 'svg', // 渲染方式，可以是 'svg'、'canvas' 或 'html'
			      loop: true, // 是否循环播放
			      autoplay: true, // 是否自动播放
			      path: '/static/svgJson/ball.json' // 动画 JSON 文件的路径
			});
			this.screen=lottie.loadAnimation({
			      container: document.getElementById('screen'), // 动画容器
			      renderer: 'svg', // 渲染方式，可以是 'svg'、'canvas' 或 'html'
			      loop: true, // 是否循环播放
			      autoplay: true, // 是否自动播放
			      path: '/static/svgJson/screen.json' // 动画 JSON 文件的路径
			});
			this.square=lottie.loadAnimation({
			      container: document.getElementById('screen2'), // 动画容器
			      renderer: 'svg', // 渲染方式，可以是 'svg'、'canvas' 或 'html'
			      loop: true, // 是否循环播放
			      autoplay: true, // 是否自动播放
			      path: '/static/svgJson/screen2.json' // 动画 JSON 文件的路径
			});
				
		},
		beforeDestroy() {
		      if (this.mutationObserver) {
		        this.mutationObserver.disconnect();
		      }
		}
	}
</script>

<style lang="scss">
	.back{
		position: fixed;
	}
	.left{
		width:22% ;//285px
		position: fixed;
		top: 0;
		left: 0;
		// background: linear-gradient(-45deg,#ABFF84,#266FFF);
		// background-color: #FFFFFF30;
		height: 100%;//800px
	}
	.lefthead{
		display: flex;
		padding: 5%;//15px
	}
	.createSession{
		border: solid 1.5px #266FFF;
		border-radius: 18px;
		margin-left: 17px;
		height: 50px;
		font-size: 18px;
		// color:#266FFF;// #377F7F;
		width: 85%;//250px
		// background: linear-gradient(to right,#A7FB83,#ABFF84);
		// -webkit-background-clip: text,border-box;/*将设置的背景颜色限制在文字中*/
		// -webkit-text-fill-color: transparent;/*给文字设置成透明*/
		// font-weight: bold;
		letter-spacing: 5px;
		background-color:#6FE6A400;
	}
	.diaHistory{
		width: 100%;//285px
		color: #415058;
		margin-top: 8%;//20px
	}
	.hisTitle{
		font-size: 17px;
		margin-left: 6%;//20px
		display: flex;
	}
	.historyList{
		height: 300px;
		// overflow-y: scroll;
		overflow-x: clip;
		margin-top: 4%;//10px
		font-size: 14px;
	}
	#hisChose{
		font-size: 16px;
		text-align: center;
		margin-top: 5%;//10px
		color: #266FFF;
	}
	.historyEle{
		display: flex;
		margin-top: 4%;//15px
	}
	.hisText{
		display: flex;
		margin-left: 1%;//20px
		font-size: 15px;
		justify-content: center;
		width: 85%;//220px
		cursor: pointer;
		margin-right: 0%;
	}
	.statuIcon{
		font-size: 12px;
		background:linear-gradient(-45deg,#ABFF84,#266FFF);
		-webkit-background-clip: text,border-box;
		-webkit-text-fill-color: transparent;
		// color:#ABFF84;
		border-radius: 10px;
		text-align: center;
		padding: 2px;
		margin-top: 3px;
	}
	.nameInput:focus{
		border: solid 1px #377F7F;
		outline: #377F7F;
		height:30px;
		width:90%;//205px
		padding-left:10px;
		border-radius:20px;
		background-color:#54BCBD10;
		margin-left: 0px;
		margin-right: 0px;
		overflow-x: scroll;
		caret-color: #266FFF;
		color: #DADDE5;//#415058
	}
	.right{
		position: absolute;
		left: 22%;//285px
		top: 3%;
		width: 75%;//1130px;
		height: 94%;//780px;
		border-radius: 30px ;
		// margin-left: 20px;
		background-color: #FAFBFF;
		// border: solid 1px #07D4E1;
		box-shadow:  0px 0px 6px 0px  #41505860;
	}
	.user{
		display: flex;
		position: absolute;
		top: 85%;//640px
		left: 17%;//50px
		cursor: pointer;
		color: #415058;
		// font-weight: bold;
	}
	.diaPic{
		margin-top: 10px;
	}
	.dialogueList{
		max-height:  61vh;//480px
		overflow-y: scroll;
		margin-left: 8%;//100px;
	}
	.firstQus{
		font-size: 18px;
		line-height: 30px;
		background:linear-gradient(-45deg,#ABFF84,#266FFF);
		-webkit-background-clip: text,border-box;/*将设置的背景颜色限制在文字中*/
		-webkit-text-fill-color: transparent;/*给文字设置成透明*/
		margin-left: 26%;//300px;
		margin-top: 7%//60px;
	}
	.userDia{
		display: flex;
		// margin-left: 440px;
		margin-top: 20px;
	}
	.userContent{
		font-size: 16px;
		white-space: pre-wrap;
		overflow: hidden;
		display: -webkit-box; 
		-webkit-box-orient: vertical; 
		max-width: 30vw;//500px;
		margin-top: 5px;
		line-height: 25px;
		color: #415058;
	}
	.time{
		text-align: center;
		color: #9A9A9A;
		font-size: 14px;
		width: 83%;//900px;
		margin-top: 20px;
	}
	// .usertime{
	// 	margin-left: 20px;
	// 	color: #9A9A9A;
	// 	font-size: 14px;
	// }
	.aiDia{
		display: flex;
		margin-top: 20px;
	}
	.aiContent{
		font-size: 16px;
		background:linear-gradient(-45deg,#ABFF8420,#266FFF20);// #F4F6FC;
		border-radius: 12px;
		max-width: 57vw;//800px;
		min-width: 100px;
		padding: 15px;
		white-space: pre-wrap;
		overflow: hidden;
		display: -webkit-box; 
		-webkit-box-orient: vertical; 
		line-height: 25px;
	}
	// .aitime{
	// 	margin-left: 270px;
	// 	color: #9A9A9A;
	// 	font-size: 14px;
	// }
	.boxButtom{
		display: flex;
	}
	.option{
		margin-top: 5px;
		margin-left: 10px;
		cursor: pointer;
	}
	.righthead{
		margin-left: 46%;//480px;
		font-weight: bold;
		width: 20%;//100px;
		height: 8%;//90px;
		line-height: 62px;
		font-size: 22px;
		text-align: center;
		color: #415058;
		cursor: pointer;
	}
	.righthead .changeText {
	    visibility: hidden;
	    // background-color:#81EE9A40;
	    text-align: center;
	    border-radius: 6px;
	    font-size: 14px;
	    top: 0;//-55%;//-50px;
	    left: 0;//30%;//32px;
	    margin-left: 0;//-60px;
		padding: 10px;
		z-index: 1;
		}
	// .righthead .changeText::after {
	//     content: "";
	//     position: absolute;
	//     bottom: 100%;
	//     left: 50%;
	//     margin-left: -5px;
	//     border-width: 5px;
	//     border-style: solid;
	//     border-color: transparent transparent #81EE9A40 transparent;
	// }
	.righthead:hover .changeText {
	    visibility: visible;
	}
	.inputcontent{
		background-color: #FFFFFF30;
		display: flex;
		height: 20vh;//170px;
		width: 62vw;//800px;
		border: solid 1px #266FFF;//#DCE0E7;
		box-shadow: 0px 0px 6px 0px #266FFF50;//#00000040;
		border-radius: 28px;
		position: absolute;
		top: 70vh;//590px;
		left: 10%;//140px;
		// z-index: 1;
	}
	textarea{
		line-height: 20px;
		height: 12vh;//100px;
		width: 39vw;//560px;
		position: relative;
		top: 32%;//52px;
		left: -30%;//-200px;
		// padding-left: 10px;
		// padding-top: 50px;
		white-space: pre-wrap;
		caret-color: #266FFF;
		// color: whitesmoke;
	}
	.photo{
		position: relative;
		top:11%;//20px;
		left:77%;//630px;
		cursor: pointer;
	}
	.video{
		position: relative;
		top:11%;//20px;
		left:79%;//630px;
		cursor: pointer;
	}
	.send{
		height: 32px;
		width: 68px;
		background:linear-gradient(-45deg,#81EE9A,#266FFF);//#377F7F,#377F7F70
		color: #FFF;
		font-size: 14px;
		line-height: 32px;
		border-radius: 20px;
		border: none;
		position: relative;
		top:11%;//20px;
		left:70%;//590px;
	}
	.picChose{
		position: absolute;
		left:80%;//640px;
		top: 42%;//70px;
	}
	.picDel{
		position: absolute;
		top: 35%;//60px;
		left:92.5%;//750px;
		cursor: pointer;
	}
	.hidden{
		display: none;
	}
	img:not([src]){
		display: none;
	}
	.putAlert{
		position: absolute;
		top: 46%;
		left: 24%;
		height: 50%;
		width: 50%;
		text-align: center;
		color: #377F7F;
	}
	.analysis{
		visibility: hidden;
		opacity: 0;
	}
	.anaButton{
		opacity: 0;
		visibility: hidden;
		position: absolute;
		box-shadow: 0 0 6px 0 #81EE9A50;
		border-radius: 30px;
		width: 5%;
		height: 4%;
		line-height: 30px;
		text-align: center;
		font-size: 18px;
		cursor: pointer;
		top:65%
	}
	.anahead{
		position: absolute;
		left: 44%;//480px;
		top: 2%;
		font-weight: bold;
		width: 20%;//100px;
		height: 8%;//90px;
		line-height: 62px;
		font-size: 22px;
		text-align: center;
		color: #7898FF;
		cursor: pointer;
	}
	.anachangeText{
		position: relative;
		font-size: 14px ;
		top: -94%;
		left: 33%;
	}
	.anaAI{
		visibility: hidden;
		opacity: 0;
	}
	.anaAIpic{
		position: absolute;
		top: 15%;
		left: 20%;
		height: 30vh;
		width: 25vw;
		box-shadow:0 0 6px 0 #215476 ;
	}
	.anaAIcon{
		position: absolute;
		top: 15%;
		left: 55%;
		background:linear-gradient(-45deg,#07D4E1,#7898FF);
		-webkit-background-clip: text,border-box;
		-webkit-text-fill-color: transparent;
		font-size: 18px;
		width: 40vw;
		height:75vh ;
		white-space: pre-wrap;
		border: solid #7898FF 1px ;
		box-shadow: 0 0 6px 0 #798AC050;
		border-radius: 30px;
		padding: 12px;
		line-height: 25px;
		
	}
	.rebegin{
		position: absolute;
		top: 88%;
		left: 84%;
		padding: 5px;
		border-radius: 20px;
		color: #07D5E2;
		cursor: pointer;
	}
	.setting{
		position: absolute;
		top: 7vh;
		left: 98vw;
	}
</style>
