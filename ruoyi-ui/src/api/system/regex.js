import request from '@/utils/request'

// 查询正则达列表
export function listRegex(query) {
  return request({
    url: '/system/regex/list',
    method: 'get',
    params: query
  })
}

// 查询正则达详细
export function getRegex(regexId) {
  return request({
    url: '/system/regex/' + regexId,
    method: 'get'
  })
}

// 新增正则达
export function addRegex(data) {
  return request({
    url: '/system/regex',
    method: 'post',
    data: data
  })
}

// 修改正则达
export function updateRegex(data) {
  return request({
    url: '/system/regex',
    method: 'put',
    data: data
  })
}

// 删除正则达
export function delRegex(regexId) {
  return request({
    url: '/system/regex/' + regexId,
    method: 'delete'
  })
}
