<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="goodsInfo.num" placeholder="请输入溯源码" style="width: 500px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="溯源码" prop="id" align="center" width="200" >
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ timeStampToTime(row.changeTime)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.goodsType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="质检状态" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.QualityState }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所在地点" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.location }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" top="1%" style="height:95%;overflow-y: auto;" @close="sflag = true" @open="deletePicFlag=false">
    <div v-loading="sflag">
      <el-timeline>
        <el-timeline-item v-for="i in tempList" :key="i.changeTime" :timestamp=timeStampToTime(i.changeTime) placement="top">
          <el-card style="width: 500px">
            <span style="margin-left: 20px">{{'商品名称： ' + i.name }}<el-tag style="margin-left: 10px">{{i.goodsType }}</el-tag></span>
            <span style="float: right">{{'商品状态：' }}<el-tag type="success">{{i.userType}}</el-tag></span>
            <el-divider direction="horizontal"></el-divider>
            <span style="margin-left: 20px">{{'加工单号： ' + i.processNum }}</span>
            <p style="margin-left: 20px">{{'加工内容： ' + i.processContent }}</p>
            <el-divider direction="horizontal"></el-divider>
            <span style="margin-left: 20px">{{'运输单号： ' + i.transportNum }}</span>
            <span style="float: right;margin-right: 20px">{{'运输方式： ' + i.transportWay }}</span>
            <el-divider direction="horizontal"></el-divider>

            <span style="margin-left: 20px">{{'销售价格： ' + i.price}}</span>
            <p></p>
            <span style="margin-left: 20px">{{'当前位置： ' + i.location }}<el-tag style="margin-left: 10px" :type="i.location | dangerTypeFilter($root)">{{i.location | dangerFilter($root)}}</el-tag></span>
            <span style="float: right;margin-right: 20px">{{'负责人： ' + i.principal }}</span>
            <el-divider direction="horizontal"></el-divider>
            <p style="margin-left: 20px">{{'备注信息： ' + i.note }}<el-tag type="success" style="float: right">{{'质检'+i.QualityState }}</el-tag></p>

          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-dialog>
  </div>
</template>

<script>
  import waves from '@/directive/waves' // waves directive
  import { parseTime } from '@/utils'
  import Pagination from '@/components/Pagination' // 分页操作
  import qs from 'qs'
  import myUpload from 'vue-image-crop-upload/upload-2.vue'
  import {addDangerUser, addQueryHistory,} from "@/api/user";
  import Qs from "qs";


  export default {
    name: 'goodsQuery',
    components: { Pagination,myUpload},
    directives: { waves },
    filters:{
      typeFilter(status){
        const statusMap = {
          1: '生产',
          2: '加工',
          3: '运输',
          4: '销售',
        }
        return statusMap[status]
      },
      dangerFilter(status,area) {
        for (let i of area.dangerArea) {
          if (i === status) {
            return "风险区"
          }
        }
        return "安全区"
      },
      dangerTypeFilter(status,area) {
        for (let i of area.dangerArea) {
          if (i === status) {
            return "danger"
          }
        }
        return "success"
      },
    },
    data() {
      return {
        tableKey: 0,
        list: null,
        total: 0,
        cardNum: 0,
        sflag:true,
        targetApi:this.backApi,
        listLoading: false,
        tempList:[],
        listQuery: {
          current: 1,
          size: 20,
          total:undefined,
          dataList: {
            id:'',
            num:'',
            name: '',
            goodsType:'',
            processNum:'',
            processContent:'',
            transportNum:'',
            transportWay:'',
            price:'',
            note:'',
            location: '',
            userType:'',
            principal:'',
            QualityState:'',
            changeTime:'',
          },
          sort: '+id'
        },

        sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
        statusOptions: [{label:'未标记',value:0},{label:'标记',value:1}],//标记状态选择
        goodsInfo: {
          id:'',
          num:'',
          name: '',
          goodsType:'',
          processNum:'',
          processContent:'',
          transportNum:'',
          transportWay:'',
          price:'',
          note:'',
          location: '',
          userType:'',
          principal:'',
          QualityState:'',
          changeTime:'',
          dState:'无风险',
        },

        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          open: '溯源信息',
          create: '新建'
        },
        dialogPvVisible: false,
        pvData: [],
        rules: {
          username: [{ required: true, message: '必须填写用户名', trigger: 'change' }],
          email: [{ required: true, message: '必须填写邮箱', trigger: 'blur' }],
          location: [{ required: true, message: '必须填写所在地', trigger: 'change' }],
        },
        downloadLoading: false
      }
    },
    created() {
      //this.getList()
    },
    methods: {
      getList(){},
      utypeFilter(status){
        const statusMap = {
          1: '生产',
          2: '加工',
          3: '运输',
          4: '销售',
        }
        return statusMap[status]
      },

      handleFilter() {
        let self = this
        this.listQuery.page = 1
        // this.getList()
        this.goodsBlock.data().contract.methods.getbnum(this.goodsInfo.num).call((error,result) =>{
          this.cardNum = result;
          this.goodsBlock.data().contract.methods.getProductName(this.cardNum,this.goodsInfo.num).call(function (error,result) {
            self.goodsInfo.name = result;
          })
          this.goodsBlock.data().contract.methods.getProductChangeTime(this.cardNum,this.goodsInfo.num).call(function (error,result) {
            self.goodsInfo.changeTime = result;
          })
          this.goodsBlock.data().contract.methods.getProductGoodsType(this.cardNum,this.goodsInfo.num).call(function (error,result) {
            self.goodsInfo.goodsType = result;
          })
          this.goodsBlock.data().contract.methods.getProductQualityState(this.cardNum,this.goodsInfo.num).call(function (error,result) {
            self.goodsInfo.QualityState = result;
          })
          self.goodsInfo.dState="无风险"
            for(let i = 1;i <= self.cardNum;i++){
              let gList={
                location: '',
              }
              self.goodsBlock.data().contract.methods.getProductLocation(i,self.goodsInfo.num).call(function (error,result) {
                self.goodsInfo.location = result;
                for(let j of self.$root.dangerArea){
                  if(self.goodsInfo.location === j){
                    self.goodsInfo.dState = "有风险"
                    break
                  }
                }
                self.list = [self.goodsInfo]
              })
            }
          let data = Qs.stringify({
            account: this.$root.userData.account,
            num: this.goodsInfo.num,
            queryTime: this.getCurrentTime(),
            dState: this.goodsInfo.dState
          });
          addQueryHistory(data).then(res=>{
            console.log(res)
          })
        })


      },

      /**
       * 控制编辑内容的弹框
       * @param row
       */
      handleUpdate(row) {
        this.tempList = []
        this.goodsInfo = Object.assign({}, row) //把那一行的内容存到temp里面
        this.showGoodsTrace()
        this.dialogStatus = 'open'
        this.dialogFormVisible = true
      },
      /**
       * 查看详情的代码
       */
      showGoodsTrace(){
        let self = this
        this.goodsBlock.data().contract.methods.getbnum(this.goodsInfo.num).call(function (error,result) {
          self.cardNum = result;
          for(let i = 1;i <= self.cardNum;i++){
            let gList={
              name: '',
              goodsType:'',
              processNum:'',
              processContent:'',
              transportNum:'',
              transportWay:'',
              price:'',
              note:'',
              location: '',
              userType:'',
              principal:'',
              QualityState:'',
              changeTime:'',
            }
            self.goodsBlock.data().contract.methods.getProductName(i,self.goodsInfo.num).call(function (error,result) {
              gList.name = result;
              self.goodsBlock.data().contract.methods.getProductChangeTime(i,self.goodsInfo.num).call(function (error,result) {
                gList.changeTime = result;
                self.goodsBlock.data().contract.methods.getProductGoodsType(i,self.goodsInfo.num).call(function (error,result) {
                  gList.goodsType = result;
                  self.goodsBlock.data().contract.methods.getProductProcessNum(i,self.goodsInfo.num).call(function (error,result) {
                    gList.processNum = result;
                    self.goodsBlock.data().contract.methods.getProductProcessContent(i,self.goodsInfo.num).call(function (error,result) {
                      gList.processContent = result;
                      self.goodsBlock.data().contract.methods.getProductTransportNum(i,self.goodsInfo.num).call(function (error,result) {
                        gList.transportNum = result;
                        self.goodsBlock.data().contract.methods.getProductTransportWay(i,self.goodsInfo.num).call(function (error,result) {
                          gList.transportWay = result;
                          self.goodsBlock.data().contract.methods.getProductPrice(i,self.goodsInfo.num).call(function (error,result) {
                            gList.price = result;
                            self.goodsBlock.data().contract.methods.getProductNote(i,self.goodsInfo.num).call(function (error,result) {
                              gList.note = result;
                              self.goodsBlock.data().contract.methods.getProductLocation(i,self.goodsInfo.num).call(function (error,result) {
                                gList.location = result;
                                self.goodsBlock.data().contract.methods.getProductUserType(i,self.goodsInfo.num).call(function (error,result) {
                                  gList.userType = self.utypeFilter(result);
                                  self.goodsBlock.data().contract.methods.getProductPrincipal(i,self.goodsInfo.num).call(function (error,result) {
                                    gList.principal = result;
                                    self.goodsBlock.data().contract.methods.getProductQualityState(i,self.goodsInfo.num).call(function (error,result) {
                                      gList.QualityState = result;
                                      console.log(self.goodsInfo)
                                      self.$forceUpdate()
                                      self.tempList.push(gList)
                                      if (i == self.cardNum)
                                        self.sflag = false;
                                    })
                                  })
                                })
                              })
                            })
                          })
                        })
                      })

                    })
                  })
                })
              })
            })
          }
        })
      },

      sortChange(data) {
        const { prop, order } = data
        if (prop === 'id') {
          this.sortByID(order)
        }
      },
      sortByID(order) {
        if (order === 'ascending') {
          this.listQuery.sort = '+id'
        } else {
          this.listQuery.sort = '-id'
        }
        this.handleFilter()
      },

      getCurrentTime() {
        //获取当前时间并打印
        let yy = new Date().getFullYear();
        let mm = new Date().getMonth()+1;
        let dd = new Date().getDate();
        let hh = new Date().getHours();
        let mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
        let ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
        let gettime = yy+'/'+mm+'/'+dd+' '+hh+':'+mf+':'+ss;
        return gettime
      },

      /**
       * 把时间戳转换为时间
       * @param te
       * @returns {string}
       */
      timeStampToTime (te){
        if(te == ''){
          return '';
        }else if(te.length == 10){
          var time = new Date(te * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
          var y = time.getFullYear();
          var m = time.getMonth() < 9 ? '0' + (time.getMonth() + 1) : time.getMonth() + 1;
          var d = time.getDate() < 10 ? '0' + time.getDate() : time.getDate();
          var h = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
          var mm = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
          var s = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
          var timedate = y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
          return timedate;
        }else{
          var time = new Date(te);
          var y = time.getFullYear();
          var m = time.getMonth() < 9 ? '0' + (time.getMonth() + 1) : time.getMonth() + 1;
          var d = time.getDate() < 10 ? '0' + time.getDate() : time.getDate();
          var h = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
          var mm = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
          var s = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
          var timedate = y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
          return timedate;
        }
      },

      getSortClass: function(key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      }
    }
  }
</script>
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 250px;
    height: 250px;
    display: block;
    margin-bottom: 10px;
  }
  ::-webkit-scrollbar {
    width: 10px !important;
    height: 10px !important;
    border-radius: 5px;
  }
  ::-webkit-scrollbar-thumb {
    border-radius: 5px;
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.2);
    /* 滚动条的颜色 */
    background-color: #e4e4e4;
  }
  .myimage {
    /* 自适应正方形 */
    width: 100%;
    display: block;
    height: 0;
    padding-bottom: 100%;
  }
  .demo-image__lazy .el-image {
    display: block;
    min-height: 200px;
    margin-bottom: 10px;
  }
  .myimage >>> .el-image__inner {
    position: absolute;
  }
  .image {
    width: 100%;
    position: absolute;
  }
  .card{
    margin-bottom: 30px;
  }

</style>
