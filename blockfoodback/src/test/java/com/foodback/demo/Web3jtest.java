package com.foodback.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/eth")
public class Web3jtest {
    @Autowired
    private Web3j web3j;


    @GetMapping("height")
    public long getHeight() {
        try {
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            long blockHeight = blockNumber.getBlockNumber().longValue();
            Web3ClientVersion web3clientversion = web3j.web3ClientVersion().send();
            String clientVersion = web3clientversion.getWeb3ClientVersion();//获取版本号判断是否连接成功
            System.out.println("web3clientVersion : " + clientVersion);
            List<String> addressList = web3j.ethAccounts().send().getAccounts();//获取以太坊的账户
            System.out.println("addressList : "+addressList);
            BigInteger balance0 = web3j.ethGetBalance(addressList.get(0), DefaultBlockParameterName.LATEST).send().getBalance();//获取eth
            BigInteger balance1 = web3j.ethGetBalance(addressList.get(1), DefaultBlockParameterName.LATEST).send().getBalance();
            System.out.println("balance0 : " + addressList.get(0) + " : " + balance0);
            System.out.println("balance1 : " + addressList.get(1) + " : " + balance1);

            //指定地址发起的交易数量
            BigInteger mynounce = web3j.ethGetTransactionCount(addressList.get(0), DefaultBlockParameterName.LATEST).send().getTransactionCount();
            //为一个单位的gas出多少eth
            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
            //单次交易中使用gas的上限
            BigInteger gasLimit = BigInteger.valueOf(21000);
            BigInteger value = Convert.toWei(BigDecimal.valueOf(0.5), Convert.Unit.ETHER).toBigInteger() ;
            Transaction mytesttransaction= Transaction.createEtherTransaction(addressList.get(0),mynounce, gasPrice, gasLimit, addressList.get(1),value);
            System.out.println("mytesttransaction : ");
            printTransaction(mytesttransaction);
            //发送交易，获取交易哈希
            String txhash = web3j.ethSendTransaction(mytesttransaction).send().getTransactionHash();
            System.out.println("txhash : " + txhash);
            //交易对应的收据，这个对于智能合约交易非常有用，对普通交易没多大用
            TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(txhash).send().getTransactionReceipt().get();
            System.out.println("transactionReceipt : " + transactionReceipt);

            //交易后的余额
            balance0 = web3j.ethGetBalance(addressList.get(0), DefaultBlockParameterName.LATEST).send().getBalance();
            balance1 = web3j.ethGetBalance(addressList.get(1), DefaultBlockParameterName.LATEST).send().getBalance();
            System.out.println("after Transaction:");
            System.out.println("balance0 : " + addressList.get(0) + " : " + balance0);
            System.out.println("balance1 : " + addressList.get(1) + " : " + balance1);
            //获取当前最新区块，并获取它上面的所有交易
            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
            System.out.println("latestBlock: " + latestBlock.getHash());
            //获取区块上的所有交易
            List<EthBlock.TransactionResult> txlist = latestBlock.getTransactions();
            System.out.println("get TransactionBy Block :" );
            for (EthBlock.TransactionResult transactionResult : txlist) {
                System.out.println(transactionResult.get());
            }

            return blockHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
    public static void printTransaction(Transaction x) {
        //打印交易信息
        System.out.println("From:"+x.getFrom());
        System.out.println("GasLimit:"+x.getGas());
        System.out.println("Nonce:"+x.getNonce());
        System.out.println("GasPrice:"+x.getGasPrice());
        System.out.println("To:"+x.getTo());
        System.out.println("Value:"+x.getValue());
    }


}
