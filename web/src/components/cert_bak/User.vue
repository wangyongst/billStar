<template>
  <div class="backgroundColorWhite">
    <div class="t010" style="display: table">
      <template v-if="user.avatar.length>0">
        <img width="100%" height="auto" v-bind:src="user.avatar">
      </template>
      <template v-else>
        <img width="100%" height="auto" v-bind:alt="user.name" class="mui-media-object mui-pull-left" src="../../assets/img/defaultPerson.jpg">
      </template>
    </div>
    <ul class="mui-table-view t100">
      <li class="mui-table-view-cell"><i class="fa fa-user-circle-o marginRight10" aria-hidden="true"></i> {{user.name}}</li>
      <li class="mui-table-view-cell"><i class="fa fa-mobile marginRight10"></i>{{user.mobile}}</li>
      <li class="mui-table-view-cell"><i class="fa fa-envelope-square marginRight10"></i>{{user.email}}</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: 'user',
    data() {
      return {
        user: {}
      }
    },
    mounted: function () {

      this.getUser();
    },
    methods: {
      getUser() {
        this.$http.get(this.$appConfig.urlPrefix + '/userQuery/getVO?id=' + this.$route.params.id).then(response => {
          this.user = response.data.data;
        }).catch(e => {
          alert(JSON.stringify(e));
        }).finally(o => {
          console.log(JSON.stringify(o));
        })
      }
    }
  }
</script>
