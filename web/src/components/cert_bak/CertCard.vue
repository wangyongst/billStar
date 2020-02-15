<template>
  <div class="certCard">
    <div class="mui-row certBox">
      <div class="mui-col-sm-6 mui-col-lg-6 mui-col-xs-6 left">
        <h4 class="title">
          <i class="fa fa-gg" style="color:#39E"></i>
          <template v-if="cert.expired"><i class="fa fa-font-awesome" style="color:red"></i></template>
          <span>{{cert.typeName}}</span>
        </h4>
        <router-link class="text" :to="'/cert/'+cert.id">
          <div class="imageBox">
            <img v-bind:src="cert.imgUrl" class="image" style="" v-bind:alt="cert.name"/>
          </div>
        </router-link>
      </div>
      <div class="mui-col-sm-6 mui-col-lg-6 mui-col-xs-6 right">
        <h4 class="title">{{cert.name}}</h4>
        <p class="desc">{{cert.description}}</p>
        <p><i class="fa fa-clock-o"></i> 过期时间: {{this.commonUtils.formatDate(cert.expireDate)}}</p>
        <p class="fontSize08em">
          <span class=""><i class="fa fa-user-o"></i> {{cert.creatorName}}</span>
        </p>
        <p class="fontSize08em">
          <span class=""><i class="fa fa-calendar-plus-o"></i> {{this.commonUtils.formatTime(cert.createDate)}}</span>
        </p>
      </div>

    </div>
  </div>
</template>

<script>
  export default {
    name: 'certCard',
    props: {
      cert: {
        require: true,
      }
    },
    data() {
      return {}
    },
    mounted: function () {


    },
    methods: {}
  }
</script>
