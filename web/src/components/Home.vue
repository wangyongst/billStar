<template>
  <div>
    <el-row>
      <el-col :span="12" :offset="4">
        <img src="https://www.w3school.com.cn/i/eg_chinarose.jpg" height="240" width="800">
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12" :offset="4">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
          </div>
          <div>
            <el-row>
              <el-col :span="8">
                <div class="grid-content bg-purple">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary" @click="create()">新生收费</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary" @click="renewals()">续费</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary" @click="toPage('courses')">课程表</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="8">
                <div class="grid-content bg-purple">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary" @click="toPage('work/details')">收费查询</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary" @click="toPage('work/arrears')">欠费查询</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple"></div>
                <el-card class="box-card">
                  <div>
                    <el-button type="primary" @click="toPage('student/change')">转班管理</el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12" :offset="4">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
          </div>
          <div>
            <el-row>
              <el-col :span="5">
                <div class="grid-content bg-purple">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>今日营业额</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>本月营业额</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="grid-content bg-purple"></div>
                <el-card class="box-card">
                  <div>
                    <el-button type="primary"><p>121212</p>今日新增</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="4">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>在校生</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="grid-content bg-purple-light">
                  <el-card class="box-card">
                    <div>
                      <el-button type="primary"><p>121212</p>科次</el-button>
                    </div>
                  </el-card>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <NewStudent></NewStudent>
    <RenewalsStudent></RenewalsStudent>
  </div>
</template>


<script>

  import NewStudent from './dialog/NewStudent';

  import RenewalsStudent from './dialog/RenewalsStudent';

  export default {
    name: 'Home',
    components: {NewStudent,RenewalsStudent},
    data() {
      return {}
    },
    methods: {
      create() {
        eventBus.$emit("createStudent");
      },

      renewals() {
        eventBus.$emit("renewalsStudent");
      },

      change() {
        eventBus.$emit("changeStudent");
      },

      toPage(url) {
        const _this = this;
        url = "/" + url;
        console.log("url=" + url);
        _this.$router.push({path: url});
      }
    }
  }
</script>
