<template>
  <div class="backgroundColorWhite">
    <ul v-for="type in typeList" class="mui-table-view">
      <li class="mui-table-view-cell mui-media ">
        <div class="name FL">{{type.name}}</div>
        <div class="FR">
          <i class="fa fa-edit marginRight10" v-on:click="editType(type)"></i>
          <i class="fa fa-trash-o colorCloseRed" v-on:click="deleteType(type)"></i>
        </div>
      </li>
    </ul>
    <b-button variant="outline-primary" v-on:click="create" class=" marginTop10 marginBottom10">新增类型</b-button>

    <!-- edit modal -->
    <b-modal ref="editModal" v-bind:title="'修改类型:'+selectedItem.name" ok-title="确定修改" cancel-title="取消" @ok="updatePost">
      <div>
        <input type="text" class="form-control" v-model="newName" placeholder="请输入新的类型名"/>
      </div>
    </b-modal>
    <!-- create modal -->
    <b-modal ref="createModal" title="新增类型" ok-title="保存" cancel-title="取消" @ok="createPost">
      <div>
        <input type="text" class="form-control" v-model="newName"/>
      </div>
    </b-modal>
    <!-- delete modal   -->
    <b-modal ref="deleteModal" title="删除类型" ok-title="确定删除" ok-variant="danger" cancel-title="取消" @ok="deleteGet">
      <p class="t100">确定删除: {{selectedItem.name}} ?</p>
    </b-modal>
    <!--    alert-->
  </div>
</template>

<script>
  export default {
    name: 'certTypes',
    data() {
      return {
        typeList: [],
        selectedItem: {},
        newName: "",
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
        const _this = this;
        _this.httpUtils.appPost('/certType/listPage', query).then(function (res) {
          _this.typeList = res.data.list;
        }, _this.operateFail);
      }
      ,
      create() {
        this.newName = "";
        this.$refs['createModal'].show();
      },
      editType(item) {
        this.selectedItem = item;
        this.newName = item.name;
        this.$refs['editModal'].show();
      },
      updatePost(bvModalEvt) {
        bvModalEvt.preventDefault();
        const newName = this.newName;
        const selectedItem = this.selectedItem;
        const _this = this;
        if (!_this.commonUtils.isStringValid(newName)) {
          _this.commonUtils.commonAlert("新类型名不能为空");
          return;
        }
        if(newName === selectedItem.name){
          _this.commonUtils.commonAlert("类型名没有做修改。您可以点击“取消”返回列表页");
          return;
        }
        selectedItem.name = newName;
        this.httpUtils.appPost("/certType/update", selectedItem).then(
          this.operateSuccess, this.operateFail
        );

      },
      /**
       * 新增类型提交
       * @param bvModalEvt
       */
      createPost(bvModalEvt) {
        bvModalEvt.preventDefault();
        const newName = this.newName;
        const commonUtils = this.commonUtils;
        if (commonUtils.isStringValid(newName)) {
          const item = {
            name: newName,
          };
          this.httpUtils.appPost("/certType/create", item).then(this.operateSuccess,
            this.operateFail);
        } else {
          commonUtils.commonAlert("输入类型名不能为空");
        }
      },
      /**
       * 处理成功，刷新数据
       * */
      operateSuccess() {
        this.$refs['editModal'].hide();
        this.$refs['createModal'].hide();
        this.newName = "";
        this.fetchTypes();
      },
      operateFail(r) {
        this.commonUtils.commonAlert(r.msg)
      },

      /**
       * 删除类型
       * @param item
       */
      deleteType(item) {
        this.selectedItem = item;
        this.$refs['deleteModal'].show();
      },
      /**
       * 请求，删除数据
       */
      deleteGet() {
        this.httpUtils.appGet('/certType/delete?id=' + this.selectedItem.id).then(
          this.operateSuccess,
          this.operateFail
        );
      }
    }
  }
</script>
