<template>
	<view>
		
		<!-- 左侧栏 -->
		<view class="left">
			<!-- 睿途标题 -->
			<view class="lefthead">
				<img src="/static/logo.png" alt="" width='27%'/>
				<img src="/static/title_black.png" alt="" width='68%' height='18%' style="padding-top: 12px;padding-left: 6px;"/>
			</view>
			<!-- 选择栏 -->
			<view class="OptionList">
				<view class="Opts">
					<view>个人信息</view>
				</view>
				<view class="Opts">
					<view>设置</view>
				</view>
			</view>
			<!-- 返回首页 -->
			<view class="backToHome">
				<view @click="toHomepage"><img src="/static/fanhui.png" alt="错误" width='24px' height='24px' style="margin-right: 20px;"/>返回首页</view>
			</view>
		</view>
		
		<!-- 右侧信息页 -->
		<view class="perRight">
			<view class="personalTitle">
				个人信息
			</view>
			<view class="InfoList">
				<view class="InfoEle">
					<view class="InfoTitle">用户名</view>
					<view class="InfoInput">{{userForm.username}}</view>
				</view>
				<view class="InfoEle">
					<view class="InfoTitle">昵称</view>
					<input class="InfoInput" type="text" v-model="changeForm.nickname" placeholder="请输入新的昵称" :value="userForm.nickname">
				</view>
				<view class="InfoEle">
					<view class="InfoTitle">电话</view>
					<input class="InfoInput" type="text" v-model="changeForm.phone" placeholder="请输入新的电话号码" :value="userForm.phone">
				</view>
			</view>
			<view class="operations">
				<el-button @click="changeUser" type="primary">更新信息</el-button>
				<el-button @click="toLogin" type="info">退出登录</el-button>
				<el-button @click="deleteUser" type="danger">删除用户</el-button>
			</view>
			<view class="headpic">
				<img :src="userForm.headUrl" alt="错误" height='200px' width='200px' @click="changeHead" style="cursor: pointer;border-radius: 360px;" />
				<img src="/static/bianji.png" alt="" width='20px' class="edit" />
				<view style="font-size: 20px;text-align: center;margin-top: 20px;width: 200px;white-space: nowrap;overflow-x: clip;text-overflow: ellipsis;">{{userForm.nickname}}</view>
			</view>
		</view>
	</view>
</template>

<script>
import { ElButton } from 'element-plus';

 export default {
  components: {
    ElButton
  },

	  
		data() {
			return {
				userForm:{
					phone:'',
					nickname:'',
					username:'',
					headUrl:null
				},
				changeForm:{
					phone:'',
					nickname:'',
					headUrl:null
				},
				token:''
			};
		},
		methods:{
			getToken(){
				this.token=localStorage.getItem('access_token')
				console.log(this.token)
				if(!this.token){
					uni.showToast({
							icon:'none',
							title:'登录过期'
						})
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
			},
			getUser(){
				this.userForm.phone=localStorage.getItem('phone')
				this.changeForm.phone=this.userForm.phone
				this.userForm.nickname=localStorage.getItem('nickname')
				this.changeForm.nickname=this.userForm.nickname
				this.userForm.username=localStorage.getItem('username')
				let head=uni.getStorageSync('headUrl')
				if(head=='/static/head.png'){
					this.userForm.headUrl=head
				}
				else{
					this.userForm.headUrl='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+head
				}
				this.changeForm.headUrl=head
			},
			deleteUser(){
				var r=confirm("此操作无法撤回，确定删除该用户及其所有信息？");
				if (r==true)
				{
				    uni.request({
				    	url:getApp().globalData.domain+'/user/delete',
				    	method:'DELETE',
				    	header:{
				    		token:this.token
				    	},
				    	success: (res) => {
				    		if(res.statusCode==200 && res.data.code==1){
				    			console.log(res)
				    			uni.navigateTo({
				    				url:'/pages/login/login'
				    			})
				    		}
				    		
				    		else{
				    			uni.showToast({
				    				icon:'error',
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
				else
				{
				}
				
			},
			changeUser(){
				var r=confirm('确定更改用户信息吗？')
				if(r==true){
					if(this.changeForm.nickname!='' && this.changeForm.phone!=''){
						uni.request({
							url:getApp().globalData.domain+'/user/update',
							method:'PUT',
							data:this.changeForm,
							header:{
								token:this.token
							},
							success: (res) => {
								if(res.statusCode==200 && res.data.code==1){
									localStorage.setItem('phone',res.data.data.phone)
									localStorage.setItem('nickname',res.data.data.nickname)
									localStorage.setItem('headUrl',res.data.data.headUrl)
									this.getUser()
									uni.showToast({
							icon:'none',
							title:'更改成功'
						})
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
						uni.showToast({
							icon:'none',
							title:'输入不能为空'
						})
					}
				}
			},
			changeHead() {
			  uni.chooseImage({
			  	count:1,
			  	sourceType:['album'],
				sizeType:'compressed',
				// crop:{
				// 	width:'50px',
				// 	height:'50px'
				// },
			  	success:function(res){
			  		var photoUrl=res.tempFilePaths[0]
			  		console.log(photoUrl)
			  		uni.uploadFile({
			  			url:'https://www.rongtuwuyou.com:8083'+'/object/upload-io',
			  			filePath: photoUrl, 
			  			fileType: 'image', 
			  			name:"file",
			  			header:{
			  				token:this.token
			  			},
			  			success: (res) => {
			  				var res_dict=JSON.parse(res.data)
			  				this.userForm.headUrl='https://www.rongtuwuyou.com:8083'+'/object/get-stream-io?url='+res_dict.data.url
			  				this.changeForm.headUrl=res_dict.data.url
			  			},
			  			fail() {
			  				uni.showToast({
							icon:'none',
							title:'网络错误'
						})
			  			}
			  		})
			  	}.bind(this),
			  	fail() {
			  		uni.showToast({
							icon:'none',
							title:'网络错误'
						})
			  	}
			  })
			},
			toHomepage(){
				uni.navigateTo({
					url:'/pages/homepage/homepage'
				})
			},
			toLogin(){
				var r=confirm('确定退出登录吗？')
				if(r==true){
					localStorage.removeItem('access_token')
					localStorage.removeItem('phone')
					localStorage.removeItem('username')
					localStorage.removeItem('nickname')
					localStorage.removeItem('identity')
					localStorage.removeItem('headUrl')
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
			}
		},
		
		created(){
			this.getToken();
			this.getUser();
		}
	}
</script>

<style lang="scss">
	.left{
		position: fixed;
		width: 20%;//285px
		height: 100%;
		// background-color: #54BCBD30;
	}
	.lefthead{
		display: flex;
		padding: 5%;//15px
	}
	.OptionList{
		width: 100%;//285px
		font-size: 20px;
		text-align: center;
	}
	.Opts{
		margin-top: 20px;
		color: #415058;
	}
	.optChose{
		margin-top: 20px;
		color: #51A8A9;
	}
	.backToHome{
		font-size: 20px;
		position: absolute;
		top: 88%;//700px
		width: 100%;//285px
		text-align: center;
		cursor: pointer;
	}
	.perRight{
		position: absolute;
		left: 20%;//285px
		top: 0px;
		width: 66%;//960px
		margin-left:5% ;//80px
		height: 100%;
	}
	.personalTitle{
		height:13% ;//100px
		font-size: 28px;
		font-weight: 600;
		line-height: 400%;//100px
		margin-top: 6.5%;//50px
		border-bottom: solid 1px #BBBBBB;
	}
	.InfoTitle{
		font-size: 18px;
		margin-top: 2.5%;//20px
	}
	.InfoInput{
		margin-top: 1.3%;//10px
		height: 40px;
		width: 58%;//560px
		border-radius: 14px;
		box-shadow: 1px 1px 3px 0px #00000040;
		line-height: 40px;
		padding-left: 10px;
	}
	.operations{
		margin-top: 6%;//50px
		width: 58%;//560px
		button{
			width: 100%;//560px
			height: 38px;
			margin-top: 20px;
			line-height: 38px;
			color:aliceblue ;
		}
	}
	.headpic{
		position: absolute;
		top: 26%;//200px
		left: 73%;//700px
	}
	.edit{
		position: absolute;
		left: 90%;//180px
		top: 75%;//180px
	}
	// .alert{
	// 	position: absolute;
	// 	top: 100px;
	// 	left: 500px;
	// 	background: linear-gradient(to left,#DE868F10,#DE868F 20%,#DE868F 80%,#DE868F10);
	// 	padding-left: 20px;
	// 	padding-right: 20px;
	// 	height: 20px;
	// 	color: white;
	// 	font-size: 14px;
	// }
	

</style>
