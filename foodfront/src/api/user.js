import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: 'user/register',
    method: 'post',
    data
  })
}

export function emailCheck(data) {
  return request({
    url: 'getCheckCode',
    method: 'post',
    data
  })
}

export function accountDetection(data) {
  return request({
    url: 'user/accountDetection',
    method: 'post',
    data
  })
}

export function addUserGoods(data) {
  return request({
    url: 'user/addUserGoods',
    method: 'post',
    data
  })
}

export function addQueryHistory(data) {
  return request({
    url: 'user/addQueryHistory',
    method: 'post',
    data
  })
}

export function getQueryHistory(data) {
  return request({
    url: 'user/getQueryHistory',
    method: 'post',
    params:data
  })
}

export function getUserGoods(data) {
  return request({
    url: 'user/getUserGoods',
    method: 'post',
    params:data
  })
}

export function addDangerUser(data) {
  return request({
    url: 'user/addDangerUser',
    method: 'post',
    data
  })
}
