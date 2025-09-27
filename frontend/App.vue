<script>
	export default {
		globalData: {  
		            domain: 'https://www.rongtuwuyou.com/api'
		        },
		onLaunch: function() {
			console.log('App Launch')
		},
		methods:{
			alert(string) {
								let newEle = document.createElement("view");
								newEle.textContent=string;
								let newClassAttr = document.createAttribute('class');
								newClassAttr.value = alert;
								newEle.setAttributeNode(newClassAttr)
								document.appendChild(newEle)
								
			}
		}
		
	}
</script>

<style>
	::-webkit-scrollbar {
			width: 0;
			height: 0;
			background-color: transparent;
		}
	.alert{
		position: absolute;
		top: 100px;
		left: 500px;
		background: linear-gradient(to left,#DE868F10,#DE868F 20%,#DE868F 80%,#DE868F10);
		padding-left: 20px;
		padding-right: 20px;
		height: 20px;
		color: white;
		font-size: 14px;
	}

	/*每个页面公共css */
</style>
