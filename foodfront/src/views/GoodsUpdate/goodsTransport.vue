<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="goodsInfo.num" placeholder="请输入溯源码" style="width: 500px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-circle-plus-outline" @click="addGoods">
        商品上链
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
      <el-table-column label="运输单号" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.transportNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="运输方式" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.transportWay}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所在地" align="center" min-width="100">
        <template slot-scope="{row}">
          <span>{{ row.location }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" icon="el-icon-view" size="mini" @click="handleUpdate(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisibleNew" top="1%" style="height:95%;overflow-y: auto;" @close="sflag = true" @open="deletePicFlag=false">
      <el-form ref="dataForm" :rules="rules" label-width="150px" :model="goodsInfo" style="width: 500px;margin-left: 200px">
        <el-form-item label="产品溯源码" prop="num">
          <el-input v-model="goodsInfo.num"></el-input>
        </el-form-item>
        <el-form-item label="产品运输单号" prop="transportNum">
          <el-input v-model="goodsInfo.transportNum"></el-input>
        </el-form-item>
        <el-form-item label="产品运输方式" prop="transportWay">
          <el-input v-model="goodsInfo.transportWay"></el-input>
        </el-form-item>
        <el-form-item label="质检状态" prop="QualityState">
          <el-input v-model="goodsInfo.QualityState"></el-input>
        </el-form-item>
        <el-form-item label="负责人" prop="principal">
          <el-input v-model="goodsInfo.principal"></el-input>
        </el-form-item>
        <el-form-item label="产品加工地" prop="location">
          <el-input v-model="goodsInfo.location"></el-input>
        </el-form-item>
        <el-form-item label="备注信息" prop="note">
          <el-input v-model="goodsInfo.note"></el-input>
        </el-form-item>
        <div style="display: flex;justify-content: center;width: 100%;">
          <el-button ref="confirm" type="primary" @click="submitData()" style="margin-right: 30px">
            保存
          </el-button>
          <el-button @click="dialogFormVisibleNew = false">
            取消
          </el-button>
        </div>
      </el-form>
    </el-dialog>

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
  import Pagination from '@/components/Pagination' // 分页操作
  import {addDangerUser, addUserGoods, getQueryHistory, getUserGoods} from "@/api/user";
  import Qs from "qs";
  import myUpload from "vue-image-crop-upload/upload-2";

  export default {
    components: { Pagination,myUpload},
    directives: { waves },
    filters: {
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
        list: [],
        total: 0,
        cardNum: 0,
        sflag:true,
        targetApi:this.backApi,
        listLoading: false,
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
          account:'',
          sort: '+id'
        },
        labelPosition: 'right',
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
        },
        tempList:[],
        rules: {
          num: [{ required: true, message: '必须填写商品溯源码', trigger: 'blur' }],
          location: [{ required: true, message: '必须填写所在地', trigger: 'blur' }],
          transportNum: [{ required: true, message: '必须填写运输单号', trigger: 'blur' }],
          transportWay: [{ required: true, message: '必须填写运输方式', trigger: 'blur' }],
          goodsType: [{ required: true, message: '必须填写产品类型', trigger: 'blur' }],
          QualityState: [{ required: true, message: '必须填写质检状态', trigger: 'blur' }],
          principal: [{ required: true, message: '必须填写负责人', trigger: 'blur' }],
        },
        dialogFormVisible: false,
        dialogFormVisibleNew: false,

        dialogStatus: '',
        textMap: {
          open: '溯源信息',
          create: '新建商品'
        },
        dialogPvVisible: false,

      };
    },
    mounted() {
      this.goodsBlock.methods.getAccount();
    },
    created() {
      this.getList()
    },

    methods: {
      getList(){
        this.listLoading = true
        this.listQuery.account = this.$root.userData.account;
        getUserGoods(this.listQuery).then(response => {
          let tempNumList = response.dataList
          for (let i of tempNumList){
            let tempList = {
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
            }
            tempList.num = i.num
            this.goodsBlock.data().contract.methods.getbnum(tempList.num).call((error,result)=> {
              this.cardNum = result;
              this.goodsBlock.data().contract.methods.getProductName(this.cardNum, tempList.num).call((error, result) => {
                tempList.name = result;
                this.goodsBlock.data().contract.methods.getProductChangeTime(this.cardNum, tempList.num).call((error, result) => {
                  tempList.changeTime = result;
                  this.goodsBlock.data().contract.methods.getProductTransportNum(this.cardNum, tempList.num).call((error, result) => {
                    tempList.transportNum = result;
                    this.goodsBlock.data().contract.methods.getProductLocation(this.cardNum, tempList.num).call((error, result) => {
                      tempList.location = result;
                      this.goodsBlock.data().contract.methods.getProductTransportWay(this.cardNum, tempList.num).call((error, result) => {
                        tempList.transportWay = result;
                        this.list.push(tempList)
                      })
                    })
                  })
                })
              })
            })
          }

          this.total = response.total
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        })
      },
      utypeFilter(status){
        const statusMap = {
          1: '出厂',
          2: '加工',
          3: '运输',
          4: '销售',
        }
        return statusMap[status]
      },
      addGoods(){
        this.dialogStatus = 'create'
        this.dialogFormVisibleNew = true
        this.goodsInfo={
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
      },
      submitData() {
        let self = this;
        // this.goodsInfo.location = this.$root.userData.location
        this.goodsBlock.data().contract.methods.setTransport(this.goodsInfo.num,this.goodsInfo.transportNum,this.goodsInfo.transportWay,this.goodsInfo.location,this.goodsInfo.principal,this.goodsInfo.note).send({from: this.goodsBlock.methods.getAccount()[0],gas:500000}).then(res=>{
          this.goodsBlock.data().contract.methods.getnum().call(function (error,result) {
            self.goodsInfo.num = result;
            for(let i of self.$root.dangerArea){
              console.log(i)
              console.log(self.goodsInfo.location)
              if(self.goodsInfo.location === i){
                let data = Qs.stringify({
                  account: self.$root.userData.account,
                  num: self.goodsInfo.num,
                  location: self.goodsInfo.location,
                });
                addDangerUser(data).then(res=>{console.log(res)})
              }
            }
            self.goodsBlock.data().contract.methods.setProduct(self.goodsInfo.num).send({from: self.goodsBlock.methods.getAccount()[0],gas:500000}).then(res=>{
              let data = Qs.stringify({
                account: self.$root.userData.account,
                num: self.goodsInfo.num,
              });
              addUserGoods(data).then(res=>{
                console.log(res)
                self.dialogFormVisibleNew = false
                self.list = []
                self.getList()
              })
            });
          })
        });
      },
      handleFilter() {},
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
                            if(result != '')
                              gList.price = result+"元";
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
      getSortClass: function(key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      }

    }
  }
</script>

<style scoped>
  .home{
    width: 100%;
    height: 100%;
  }
  .content {
    border: 1px solid rgb(179, 179, 179);
    border-radius: 10px;
    box-shadow: 2px 2px 10px gainsboro;
    padding: 60px 50px 60px 50px;
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

