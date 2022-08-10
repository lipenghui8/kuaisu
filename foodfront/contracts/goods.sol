pragma solidity >=0.4.22 <0.9.0;
contract goods {
    uint id;//初始id
    string location;//当前位置
    uint blocknum;//当前溯源码的交易数量
    uint userType;//用户职务
    string QualityState;//质检状态
    string principal;//负责人
    string note;//备注信息

    struct Farm {//原产地信息
        string goodsId;//产品id，用来生成溯源码
        uint num;//溯源码
        string name;//产品名称
        string goodsType;//产品类型
    }

    struct Process{//加工商信息
        uint num;
        uint processNum;//加工单号
        string processContent;//加工内容
    }

    struct Transport {//运输商信息
        uint num;//溯源码
        uint transportNum;//运输单号
        string transportWay;//运输方式
    }

    struct Sale{//销售商信息
        uint num;//溯源码
        string price;//销售价格
    }

    struct Product {//产品信息
        uint num;//溯源码
        string name;//产品名称
        string goodsType;//产品类型
        uint processNum;//加工单号
        string processContent;//加工内容
        uint transportNum;//运输单号
        string transportWay;//运输方式
        string price;//销售价格
        string note;//备注信息
        string location;//途径地点
        uint userType;//商家类型
        string principal;//负责人
        string QualityState;//质检状态
        uint changeTime;//修改时间
    }

    mapping(uint => Farm) farmMap;
    mapping(uint => Transport) transportMap;
    mapping(uint => Process) processMap;
    mapping(uint => Sale) saleMap;
    mapping(uint => mapping(uint => Product)) productMap;
    mapping(uint => uint)blocknumMap;//用来统计每个溯源码有几次交易

    function setFarm(string memory goodsId, string memory name,string memory goodsType,string memory fnote,string memory flocation,string memory fprincipal,string memory fQualityState) public {
        id = uint(keccak256(abi.encodePacked(goodsId,name)))%100000000000;
        farmMap[id].num = id;
        farmMap[id].name = name;
        farmMap[id].goodsType = goodsType;
        note = fnote;
        location = flocation;
        userType = 1;
        principal = fprincipal;
        QualityState = fQualityState;
    }
    function setProcess(uint num, uint processNum,string memory processContent, string memory plocation,string memory pprincipal,string memory pnote,string memory pQualityState) public {
        processMap[num].processNum = processNum;
        processMap[num].processContent = processContent;
        principal = pprincipal;
        note = pnote;
        userType = 2;
        location = plocation;
        QualityState = pQualityState;
    }
    function setTransport(uint num, uint transportNum, string memory transportWay,string memory tlocation,string memory tprincipal,string memory tnote) public {
        transportMap[num].transportNum = transportNum;
        transportMap[num].transportWay = transportWay;
        location = tlocation;
        principal = tprincipal;
        note = tnote;
        userType = 3;
    }
    function setSale(uint num, string memory price, string memory slocation,string memory sprincipal,string memory snote,string memory sQualityState) public {
        saleMap[num].price = price;
        location = slocation;
        principal = sprincipal;
        note = snote;
        userType = 4;
        QualityState = sQualityState;
    }

    function setProduct(uint num) public {
        blocknumMap[num]++;
        blocknum = blocknumMap[num];
        productMap[blocknum][num].num = farmMap[num].num;
        productMap[blocknum][num].name = farmMap[num].name;
        productMap[blocknum][num].goodsType = farmMap[num].goodsType;
        productMap[blocknum][num].processNum = processMap[num].processNum;
        productMap[blocknum][num].processContent = processMap[num].processContent;
        productMap[blocknum][num].transportNum = transportMap[num].transportNum;
        productMap[blocknum][num].transportWay = transportMap[num].transportWay;
        productMap[blocknum][num].price = saleMap[num].price;
        productMap[blocknum][num].note = note;
        productMap[blocknum][num].location = location;
        productMap[blocknum][num].userType = userType;
        productMap[blocknum][num].principal = principal;
        productMap[blocknum][num].QualityState = QualityState;
        productMap[blocknum][num].changeTime = block.timestamp;
    }

    function getnum() public view returns (uint){
        return id;
    }
    function getbnum(uint num) public view returns(uint){
        return blocknumMap[num];
    }

    function getProductName(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].name;
    }

    function getProductGoodsType(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].goodsType;
    }

    function getProductProcessNum(uint bnum,uint num) public view returns (uint){
        return productMap[bnum][num].processNum;
    }

    function getProductProcessContent(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].processContent;
    }

    function getProductTransportNum(uint bnum,uint num) public view returns (uint){
        return productMap[bnum][num].transportNum;
    }

    function getProductTransportWay(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].transportWay;
    }

    function getProductPrice(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].price;
    }

    function getProductNote(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].note;
    }

    function getProductLocation(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].location;
    }
    function getProductUserType(uint bnum,uint num) public view returns (uint){
        return productMap[bnum][num].userType;
    }

    function getProductPrincipal(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].principal;
    }

    function getProductQualityState(uint bnum,uint num) public view returns (string memory){
        return productMap[bnum][num].QualityState;
    }

    function getProductChangeTime(uint bnum,uint num) public view returns (uint){
        return productMap[bnum][num].changeTime;
    }

    // function getProduct(uint bnum,uint num) public view returns (string memory,string memory,uint,string memory,uint,string memory,string memory,string memory,string memory,uint,string memory,string memory,uint){

    //     return (productMap[bnum][num].name,productMap[bnum][num].goodsType,productMap[bnum][num].processNum,productMap[bnum][num].processContent,productMap[bnum][num].transportNum,productMap[bnum][num].transportWay,productMap[bnum][num].price,productMap[bnum][num].note,productMap[bnum][num].location,productMap[bnum][num].userType,productMap[bnum][num].principal,productMap[bnum][num].QualityState,productMap[bnum][num].changeTime);
    // }





}
