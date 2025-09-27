<template>
	<view>
		<view class="background">
			
			<!-- 背景图片 -->
			<img src="/static/background.jpg" alt="网络错误" width='100%;//1450px' style="position: fixed;" />
			
			<!-- 薄膜 -->
			<view class="layer">
				
			</view>
			
			<!-- 登录框 -->
			<view class="logbox" >
				<view class="head">
					<img src="/static/logo.png" width='64px' height='64px' />
					<view style="font-size: 24px;height: 64px;line-height: 64px;margin-left: 24px;color:#D9E2F3">欢迎登录睿途</view>
				</view>
				<view class="inputBox" >
					<view style="font-weight: bold;">用户名</view>
					<input type="nickname" v-model="LoginForm.username" placeholder="请输入用户名" placeholder-style="color:#ACB8C9">
				</view>
				<view class="inputBox">
					<view style="font-weight: bold;">密码</view>
					<input type="password" v-model="LoginForm.password" placeholder="请输入密码" placeholder-style="color:#ACB8C9">
				</view>
				<view class='inputBox' @click="toRegister" style="font-size: 14px;cursor: pointer;">立即注册</view>
				<button @click="LoginHandle" style="margin-top: 10px">登录</button>
			</view>
		</view>
		
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				LoginForm:{
					username: "",
					password: ""
				}
			};
		},
		methods:{
			//同注册页面
			toRegister(){
				uni.navigateTo({
					url:'/pages/register/register'
				})
			},
			LoginHandle(){
				if(this.LoginForm.username && this.LoginForm.password){
					uni.request({
						url:getApp().globalData.domain+'/user/login',
						method:'POST',
						data:this.LoginForm,
						success: (res) => {
							console.log(res)
							if(res.statusCode==200 && res.data.code==1){
								localStorage.setItem("access_token",res.data.data.token)
								localStorage.setItem('phone',res.data.data.userInfo.phone)
								localStorage.setItem('username',res.data.data.userInfo.username)
								localStorage.setItem('nickname',res.data.data.userInfo.nickname)
								localStorage.setItem('identity',res.data.data.userInfo.identity)
								localStorage.setItem('headUrl',res.data.data.userInfo.headUrl)
								uni.navigateTo({
									url:'/pages/homepage/homepage'
								})
							}
							else{
								if(document.getElementsByClassName('logalert').length==0){
									let parentEle = document.getElementsByClassName('logbox')[0];
									let newEle = document.createElement("view");
									newEle.textContent=res.data.msg;
									let newClassAttr = document.createAttribute('class');
									newClassAttr.value = 'logalert';
									newEle.setAttributeNode(newClassAttr)
									let behideEle = document.getElementsByClassName('inputBox')[0];					    
									parentEle.insertBefore(newEle,behideEle)
								}
								else{
									document.getElementsByClassName('logalert')[0].innerHTML=res.data.msg
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
				}
				else{
					if(document.getElementsByClassName('logalert').length==0){
						let parentEle = document.getElementsByClassName('logbox')[0];
						let newEle = document.createElement("view");
						newEle.textContent='用户名或密码不能为空';
						newEle.className='logalert'
						let behideEle = document.getElementsByClassName('inputBox')[0];
						parentEle.insertBefore(newEle,behideEle)
					}
					else{
						document.getElementsByClassName('logalert')[0].innerHTML='用户名或密码不能为空'
					}			
				}
				
			}
		}
	}
</script>

<style lang="scss">
	.layer{
		width: 100%; //1435px
		height: 100%; //780px
		background:linear-gradient(to right,#06173E00,#06173E50,#06173E99,#06173E00);
		//z-index: 1;
		position: absolute;
		top: 0vh;
		left: 0vw;
	}
	.logbox{
		position: absolute;
		top: 20%;//150px
		left: 50%;//760px		
		border: solid 1px #D9E2F3;
		border-radius: 28px;
		background-color: #FFFFFF50;
		box-shadow: 2px 2px 12px #00000040;
		padding: 32px;
		.loginButton{
			// background:linear-gradient(-45deg,#1a2a6c,#b21f1f,#fdbb2d);
			color: #06173E90;
			font-weight: bold;
			font-size: 16px;
			margin-top: 24px;
			height: 40px;
			line-height: 40px;
			border: none;
			margin-left: 16px;
			width: 400px;
		}
	}
	.head{
		display: flex;
	}
	.inputBox{
		margin-left: 16px;
		margin-top: 10px;
		font-size: 16px;
		color: #D9E2F3;
		input{
			font-size: 16px;
			margin-top: 16px;
			padding-bottom: 16px;
			margin-bottom: 20px;
			border-bottom: solid 1px #D9E2F3;
			width: 400px;
			
		}
		
	}
	input::placeholder{
		color: #D9E2F3;
	}
	.logalert{
		font-size: 14px;
		color: #BD3124;
		padding: 5px;
		margin: 10px;
	}
	.href{
		position: fixed;
		top: 96%;
		left: 45%;
		font-weight: 100;
		font-size: 14px;
		
	}
	a:link {
		    color:#D9E2F3; /*未访问的链接颜色*/
	    }
	 
	a:visited {
		    color:#D9E2F3; /*已访问的链接颜色*/
	    }
	 
	a:hover {
		    color:  #feb47b; /*鼠标移动到链接的颜色*/
		    text-decoration: underline;
	    }
	 
	a:active {
		    color: orange; /*鼠标点击时的颜色*/
	    }
		
	a{
			text-decoration: none;
		}

</style>
