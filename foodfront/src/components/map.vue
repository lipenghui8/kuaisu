<template>
  <baidu-map
    class="map"
    :center="{ lng: 116.404, lat: 39.915 }"
    :zoom="1"
    :scroll-wheel-zoom="true"
  >
    <bm-polyline
      :path="polylinePath"
      :strokeWeight="10"
      stroke-color="blue"
      :stroke-opacity="0.5"
      :stroke-weight="2"
      :editing="true"
      @lineupdate="updatePolylinePath"
    ></bm-polyline>
    <bm-marker :position="markerPoint" :dragging="false" @click="infoWindowOpen" :icon="{url: images.various, size: {width: 40, height: 40}}">
      <bm-info-window :show="show" @close="infoWindowClose" @open="infoWindowOpen">疫情高风险地区</bm-info-window>
    </bm-marker>
  </baidu-map>
</template>


<script>
  import {getQueryHistory} from "@/api/user";
  import {BaiduMap,BmView,BmMarker,BmControl,BmAutoComplete} from "vue-baidu-map";

  export default {
    components: {
      BaiduMap,
      BmMarker,
      BmView,
      BmControl,
      BmAutoComplete,
    },
    data() {
      return {
        listQuery: {
          current: 1,
          size: 1000,
          total:undefined,
          account:'',
        },
        show:false,
        images: {
          various:require('@/assets/default/various.png'),
        },
        polylinePath: [
          { lng: 28.11, lat: -25.43 }, //分别表示经度、纬度,从上到下依次为起点到终点
          { lng: 103.51, lat: 1.18 },
          { lng: 114.03, lat: 22.32 },
          { lng: 113.41, lat: 29.58 },
          { lng: 116.404, lat: 39.915 },
        ],
        markerPoint:{lng: 103.51, lat: 1.18},
        locationList:[],
      };
    },
    mounted() {
      this.getGoodsLocation()
    },

    methods: {
      updatePolylinePath(e) {
        this.polylinePath = e.target.getPath();
      },
      addPolylinePoint() {
        this.polylinePath.push({ lng: 28.11, lat: -25.43 });
      },
      infoWindowClose() {
        this.show = false;
      },
      infoWindowOpen() {
        this.show = true;
      },

      /**
       * 获取最后一次查询的商品的位置
       */
      getGoodsLocation(){
        this.listQuery.account = this.$root.userData.account
        getQueryHistory(this.listQuery).then(response => {
          let num = response.dataList[response.dataList.length-1].num
          this.goodsBlock.data().contract.methods.getbnum(num).call((error,result) =>{
            for(let i = 1;i <= result;i++){
              this.goodsBlock.data().contract.methods.getProductLocation(i,num).call((error,result)=>{
                this.locationList.push(result)
              })
            }
          })
          this.total = response.total
          console.log(this.locationList)
          var myGeo = new BMap.Geocoder();
          myGeo.getPoint(this.locationList[0],(point)=>{
            if(point){
              console.log(point)
            }
          })
        })
      },


    },
  };
</script>

<style>
  .map {
    width: 100%;
    height: 100vh;
    overflow: hidden;
  }
</style>
