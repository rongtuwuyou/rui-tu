<template>
	<view>
		<view class="background">
			
			<!-- 背景图片 -->
			<img src="/static/background.jpg" alt="网络错误" width='100%;//1450px' style="position: fixed;"/>
			
			<!-- 薄膜 -->
			<view class="layer">
				
			</view>
			
			<!-- 注册框 -->
			<view class="regbox">
				<view class="head">
					<img src="/static/logo.png" width='64px' height='64px' />
					<view style="font-size: 24px;height: 64px;line-height: 64px;margin-left: 24px;color:#D9E2F3">注册</view>
				</view>
				<view class="inputBox" >
					<view style="font-weight: bold;">用户名</view>
					<input type="nickname" v-model="registerForm.username" placeholder="请输入用户名" placeholder-style="color:#ACB8C9">
				</view>
				<view class="inputBox">
					<view style="font-weight: bold;">电话</view>
					<input type="tel" v-model="registerForm.phone" placeholder="请输入电话" placeholder-style="color:#ACB8C9">
				</view>
				<view class="inputBox">
					<view style="font-weight: bold;">昵称</view>
					<input type="nickname" v-model="registerForm.nickname" placeholder="请输入昵称" placeholder-style="color:#ACB8C9">
				</view>
				<view class="inputBox">
					<view style="font-weight: bold;">密码</view>
					<input type="password" v-model="registerForm.password" placeholder="请输入密码" placeholder-style="color:#ACB8C9">
				</view>
				<view class='inputBox' @click="toLogin" style="font-size: 14px;cursor: pointer;">已有账号？去登录</view>
				<button @click="RegisterHandle" style="margin-top: 10px;">注册</button>
			</view>
		</view>	
	</view>
</template>

<script>
	export default {
		data() {
			return {
				registerForm:{
					username: "",
					password: '',
					phone: "",
					nickname: "",
					headUrl:'/static/head.png'
				}
			};
		},
		methods:{
			//跳转登录页面
			toLogin(){
				uni.navigateTo({
					url:'/pages/login/login'
				})
			},
			//注册
			RegisterHandle(){
				//保证不为空
				if(this.registerForm.username && this.registerForm.password && this.registerForm.phone && this.registerForm.nickname){
					uni.request({
						url:getApp().globalData.domain+'/user/register',
						method:'POST',
						data:this.registerForm,
						success: (res) => {
							console.log(res)
							if(res.data.code==1){
								localStorage.setItem("access_token",res.data.access_token)
								uni.navigateTo({
									url:'/pages/login/login'
								})
							}
							else{
								//替换错误原因，而非重复累加
								if(document.getElementsByClassName('regalert').length==0){
									//将原因插入于注册框中
									let parentEle = document.getElementsByClassName('regbox')[0];
									let newEle = document.createElement("view");
									newEle.textContent=res.data.msg;
									let newClassAttr = document.createAttribute('class');
									newClassAttr.value = 'regalert';
									newEle.setAttributeNode(newClassAttr)
									let behideEle = document.getElementsByClassName('inputBox')[0];					    
									parentEle.insertBefore(newEle,behideEle)
								}
								else{
									document.getElementsByClassName('regalert')[0].innerHTML=res.data.msg
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
					if(document.getElementsByClassName('regalert').length==0){
						let parentEle = document.getElementsByClassName('regbox')[0];
						let newEle = document.createElement("view");
						newEle.textContent="不能存在输入为空";
						newEle.className='regalert'
						let behideEle = document.getElementsByClassName('inputBox')[0];					    
						parentEle.insertBefore(newEle,behideEle)
					}
					else{
						document.getElementsByClassName('regalert')[0].innerHTML="不能存在输入为空"
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
	.regbox{
		position: absolute;
		top: 15%;//150px
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
</style>
