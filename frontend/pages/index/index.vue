<template>
	<view>
		<!-- 背景图片 -->
		<img src="/static/background.jpg" alt="网络错误" width='100%;' style="position: fixed;" />
		
		<!-- 薄膜 -->
		<view class="layer">
			
		</view>
		
		<view class="indexText">
			<view class="indexTitle">睿途</view>
			<view class="introduce">
				睿在途，安无忧
			</view>
			<view class="introduce">
				您的道路状况实时问答助手
			</view>
		</view>
		<!-- <view class="startUse" @click="ToHome">
			开始使用 >>
		</view> -->
		
		<el-button color="#334265" class="startUse" @click="ToHome" plain round>开始使用</el-button>
		
		<view class="herf">
			<a href="https://beian.miit.gov.cn/" target="_blank">备案号：蜀ICP备2025120710号-1</a>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			ToHome(){
				uni.navigateTo({
					url:'/pages/homepage/homepage'
				})
			}
		}
	}
</script>

<style>
	.layer{
		width: 100%;
		height: 100%;
		background:#FFFFFF20;
		position: fixed;
		top: 0;
		left: 0;
	}
	.indexText{
		z-index: 1;
		color: aliceblue;
		position: absolute;
		top: 25%;
		left: 15%;
		letter-spacing: 2px;
	}
	.indexTitle{
		font-size: 72px;
		font-weight: bold;
		margin-bottom: 20px;
		
	}
	.introduce{
		font-size: 32px;
		margin-left: 5px;
		margin-top: 10px;
	}
	.startUse{
		position: absolute;
		top: 60%;
		left: 15%;
		width: 200px;
		font-size: 24px;
		height: 60px;
		/* 
		margin-top: 100px;
		padding: 20px;
		
		text-align: center;
		font-weight: bold;
		cursor: pointer;
		 */
		/* background:linear-gradient(-45deg,#b21f1f,#fdbb2d);
		-webkit-background-clip: text,border-box;
		-webkit-text-fill-color: transparent; */
		
		/* border: solid 3px transparent; */
	    /* border-image: linear-gradient(to right, #ff7e5f, #feb47b) 1; */
	    /* clip-path: inset(0 round 5px); */
	}
	.herf{
		position: fixed;
		top: 96%;
		left: 15%;
		font-weight: 100;
		font-size: 14px;
		 a:link {
			    color:#FAFBFF; /*未访问的链接颜色*/
		    }
		 
		    a:visited {
			    color:#FAFBFF; /*已访问的链接颜色*/
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
	}

</style>
