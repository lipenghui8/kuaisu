const goods = artifacts.require("./goods.sol");

module.exports = function (deployer) {
  deployer.deploy(goods);
};
