<template>
  <div>
    <!--    <router-link class="text" to="home">返回</router-link>-->
    <ul v-for="user in userList" class="mui-table-view">
      <li class="mui-table-view-cell mui-media">
        <router-link class="text" :to="'/user/'+user.id" >
          <template v-if="user.avatar && user.avatar.length>0">
            <img v-bind:alt="user.name" class="mui-media-object mui-pull-left" v-bind:src="user.avatar">
          </template>
          <template v-else>
            <img v-bind:alt="user.name" class="mui-media-object mui-pull-left" src="../../assets/img/defaultPerson.jpg">
          </template>


          <div class="mui-media-body t100">
            <p>
              <span>{{user.name}}</span>
              <span class="fontSize08em1 FR" v-for="dept in user.departments">
                <span class="fontSize08em1">{{dept.name}} &nbsp;</span>
              </span>


            </p>
            <p class='mui-ellipsis'>
              <span class="fontSize08em1">{{user.mobile}}</span>
              <span v-for="role in user.roles" class="FR">
                <span class="fontSize08em1">{{role.name}}&nbsp;</span>
              </span>
            </p>
          </div>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>

  export default {
    name: 'users',
    data() {
      return {
        userList: [],
      }
    },
    mounted: function () {
      this.fetchTypes();
      const dd = this.dd;
      dd.ready(function () {
        // 调钉钉api
      });
    },
    methods: {
      fetchTypes() {
        const query = {pageNo: 0, pageSize: 100};
        this.$http.post(this.$appConfig.urlPrefix + '/userQuery/listVO', query).then(response => {
          this.userList = response.data.data.list;
        }).catch(e => {
          alert(JSON.stringify(e));
        }).finally(o => {
          console.log(JSON.stringify(o));
        })
      }
    }
  }
</script>
