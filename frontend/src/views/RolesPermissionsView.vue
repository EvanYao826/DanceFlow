<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPermissions, getRoles, type PermissionItem, type RoleItem } from '@/api/admin'
const roles = ref<RoleItem[]>([]); const permissions = ref<PermissionItem[]>([]); const loading = ref(true)
const roleLabels: Record<string, string> = { USER: '普通用户', ADMIN: '管理员', SUPER_ADMIN: '超级管理员' }
onMounted(async () => {
  try {
    roles.value = (await getRoles()).data
    permissions.value = (await getPermissions()).data
  } finally { loading.value = false }
})
</script>
<template><section class="content" v-loading="loading"><div class="title"><div><h1>角色权限</h1><p>权限码由 sys_permission.role_code 表驱动，高级角色自动继承低级角色的权限。</p></div></div><div class="grid"><el-card shadow="never"><template #header>角色列表</template><el-table :data="roles" stripe><el-table-column prop="roleCode" label="角色编码" /><el-table-column label="角色名称"><template #default="scope">{{ roleLabels[scope.row.roleCode] || scope.row.roleName }}</template></el-table-column><el-table-column label="状态"><template #default="scope"><el-tag :type="scope.row.status ? 'success' : 'info'">{{ scope.row.status ? '启用' : '停用' }}</el-tag></template></el-table-column></el-table></el-card><el-card shadow="never"><template #header>权限码</template><el-table :data="permissions" stripe><el-table-column prop="permissionCode" label="权限码" min-width="160" /><el-table-column prop="permissionName" label="名称" /><el-table-column prop="roleCode" label="最低角色" /><el-table-column prop="type" label="类型" /><el-table-column prop="path" label="路径" min-width="150" /></el-table></el-card></div></section></template>
<style scoped>.content{padding:0}.title{display:flex;justify-content:space-between;align-items:start;margin-bottom:20px}.title h1{margin:0;color:var(--df-primary-950)}.title p{margin:8px 0 0;color:#77717e}.grid{display:grid;grid-template-columns:1fr 1.4fr;gap:18px;align-items:start}@media(max-width:1000px){.grid{grid-template-columns:1fr}}</style>
