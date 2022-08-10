import request from '@/utils/request'

//获取会员列表
export function fetchUser(query) {
  return request({
    url: '/admin/user/getlist',
    method: 'get',
    params: query
  })
}

//更新会员信息
export function updateuser(data) {
  return request({
    url: '/admin/user/updatelist',
    method: 'post',
    data
  })
}


//上传头像
export function sendAvatar(picfile) {
  return request({
    url: '/admin/user/userAvatarUpload',
    method: 'post',
    data:picfile,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 覆盖文件时删除原来的文件
 * @param id
 * @returns {AxiosPromise}
 */
export function overridePic(id,picPath) {
  return request({
    url: '/admin/user/userFileDelete',
    method: 'post',
    params:{id:id,picfilePath:picPath}
  })
}

/**
 * 删除用户
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteUser(data) {
  return request({
    url: '/admin/user/deleteUser',
    method: 'post',
    data
  })
}

/**
 * 获取用户收藏歌手
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchUserSinger(query) {
  return request({
    url: '/singer/getUserCollectSinger',
    method: 'post',
    params: query
  })
}

/**
 * 获取用户收藏歌曲
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchUserSong(query) {
  return request({
    url: '/song/getUserCollectSong',
    method: 'post',
    params: query
  })
}

/**
 * 获取用户收藏歌单
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchUserSongList(query) {
  return request({
    url: '/songList/getUserCollectSongList',
    method: 'post',
    params: query
  })
}

/**
 * 删除用户收藏歌手、歌曲、歌单
 * @param userId
 * @param idStr
 * @param flag
 * @returns {AxiosPromise}
 */
export function deleteCollect(userId,idStr,flag) {
  return request({
    url: '/admin/user/deleteCollect',
    method: 'post',
    params:{userId:userId,idStr:idStr,flag:flag}
  })
}
