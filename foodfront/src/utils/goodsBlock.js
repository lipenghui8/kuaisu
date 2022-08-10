
var Web3 = require('web3');
//console.log(Web3.version);//查看web3版本

//设置web3对象
var web3 = new Web3('http://localhost:8545');//实例化web3对象
var json = require("/build/contracts/goods.json");
var abi = json["abi"];
var accounts = null;

//查看交易信息
//web3.eth.getTransaction('0x469338353e2313a8086975d437a389b9a7087110eadd103f2c452136fa25bde1').then(console.log);
var address = "0x383cF3a09f2E61Db294eE438fd7297a65c7Af4f8";

//合约实例
var contract = new web3.eth.Contract(abi,address);



export default {
  data() {
    return{
      contract,
      accounts
    };
  },

  methods:{
    getAccount(){
      web3.eth.getAccounts().then(res=>{
        this.accounts = res;
      });
      return this.accounts;
    }
  }
}






