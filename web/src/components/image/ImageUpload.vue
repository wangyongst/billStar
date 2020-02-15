<template>
  <div class="mui-row imageUpload backgroundColorWhite " v-bind:class="mainClass">
    <!--    images-->
    <template v-for="imgUrl in imageUrls">
      <div class="mui-col-sm-3 mui-col-xs-3 item">
        <img class="image" alt="#" v-bind:src="improveUrl(imgUrl)" @click="viewImage(improveUrl(imgUrl))"/>
        <template v-if="!isReadOnly">
          <span class="deleteImg fa fa-minus-circle" @click="deleteImg(imgUrl)"></span>
        </template>
      </div>
    </template>
    <!--    btn-->
    <template v-if="!isReadOnly && imageUrls.length < parseInt(maxImages)">
      <div class="mui-col-sm-3 mui-col-xs-3 item">
        <div class="imageBtn" @click="triggerUpload">
          <a class="mui-icon mui-icon-camera cameraBtn"></a>
          <p class="cameraBtnTitle">{{btnTitle}}</p>
          <!--        多图 & 单图-->
          <template v-if="parseInt(maxImages) === 1">
            <input type="file" accept=".png,.jpg,.jpeg,image/png,image/jpg,image/jpeg" v-bind:id="fileInputId"
                   @change="uploadImage" class="hidden"/>
          </template>
          <template v-else-if="parseInt(maxImages) > 1">
            <input type="file" accept=".png,.jpg,.jpeg,image/png,image/jpg,image/jpeg" multiple v-bind:id="fileInputId"
                   @change="uploadImage" class="hidden"/>
          </template>
        </div>
      </div>
    </template>
  </div>

</template>

<script>
  export default {
    name: 'imageUpload',
    props: {
      // 最多的图片数量
      maxImages: {
        required: false,
        default: 9,
      },

      // 默认的按钮下面的文字
      btnTitle: {
        require: false,
        default: "添加图片"
      },
      // 标题文字
      mainClass: {
        require: false,
        default: ""
      },
      // 是否只读，只读则不允许添加，删除
      isReadOnly: {
        require: false,
        default: false,
      },
      imageUrls: {
        require: false,
        default: function () {
          return []
        },
      },
    },
    data() {
      return {
        fileInputId: "imageUploadInput_" + new Date().getTime(),
      }
    },
    mounted: function () {

    },
    methods: {
      triggerUpload() {
        const _this = this;
        if (_this.imageUrls.length < _this.maxImages) {
          document.getElementById(_this.fileInputId).click();
        } else {
          _this.commonUtils.commonAlert("最多上传" + _this.maxImages + "张图片");
        }
      },
      improveUrl(img) {
        return this.commonUtils.improveImgUrl(img);
      },
      deleteImg(item) {
        const index = this.imageUrls.indexOf(item);
        this.imageUrls.splice(index, 1);
        this.emitUrlsChange();
      },
      viewImage(url) {
        let imgArr = [];
        const _this = this;
        _this.imageUrls.forEach(ele => {
          imgArr.push(_this.commonUtils.improveImgUrl(ele));
        });
        _this.dd.biz.util.previewImage({
          urls: imgArr,
          current: url,
          onSuccess: function (result) {
            /**/
          },
          onFail: function (err) {
          }
        })
      },

      /**
       * 更新完后把这个抛出去
       */
      emitUrlsChange() {
        const _this = this;
        _this.$emit('imageUrlsChange', this.imageUrls);
      },
      uploadImage(event) {
        const _this = this;
        let files = event.target.files;
        if (files.length + _this.imageUrls.length > _this.maxImages) {
          const rest = _this.maxImages - _this.imageUrls.length;
          _this.commonUtils.commonAlert("图片数量超限。一共允许上传" + _this.maxImages + "张图片，您只能再选择" + rest + "张");
          return;
        }

        for (let i = 0; i < files.length; i++) {
          let file = files[i];
          let param = new FormData(); //创建form对象
          param.append('file', file, file.name);//通过append向form对象添加数据
          param.append("group", "cert");
          // console.log(param.get('file')); //FormData私有类对象，访问不到，可以通过get判断值是否传进去
          let config = {
            headers: {'Content-Type': 'multipart/form-data'}
          };  //添加请求头
          _this.httpUtils.appPost('/image/upload', param, config)
            .then(function (res) {
              _this.imageUrls.push(res.data);
              _this.emitUrlsChange();
            }, function (r) {
              _this.commonUtils.commonAlert(r.msg);
            })
        }
      }
    }
  }
</script>
