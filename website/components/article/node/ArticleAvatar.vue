<template>
  <div class="Avatar bg-steam border-3 flex-align-center p-12">
    <div class="Images flex-no-shrink">
      <div class="wh-64px border-circle overflow-hidden bg-white" @click="state = 'image'">
        <cdn-img v-if="image" :image="image">
          <div v-if="editing" class="p-12" :class="{'hover-bg-a40 hover-pointer': editing}">
            <simple-svg fill="#fff" :filepath="require('~/assets/icon/icons8-person.svg')"/>
          </div>
        </cdn-img>
        <div v-else class="p-12" :class="{'hover-bg-a40 hover-pointer': editing}">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-person.svg')"/>
        </div>
      </div>
    </div>

    <div class="ml-16 flex-column flex-grow">
      <template v-if="editing">
        <input class="w-100 h4" v-model="line1"/>
        <input class="w-100 regular b-a60" v-model="line2"/>
      </template>
      <template v-else>
        <h4>{{line1}}</h4>
        <div class="regular b-a60">{{line2}}</div>
      </template>
    </div>

    <image-upload-dialog v-if="editing && state === 'image'" @on-image="onImage" @on-close="state = null"/>
  </div>
</template>

<script>
  import ContentPlace from "../../contents/ContentPlace";
  import ImageSizes from "../../core/ImageSizes";
  import ContentAvatar from "../../contents/ContentAvatar";
  import InputText from "../../core/InputText";
  import CdnImg from "../../utils/image/CdnImg";
  import ImageUploadDialog from "../../utils/image/ImageUploadDialog";

  export default {
    name: "ArticleAvatar",
    components: {ImageUploadDialog, CdnImg, InputText, ContentAvatar, ImageSizes, ContentPlace},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      editing() {
        return !!this.updateAttrs
      },
      image() {
        return this.images[0]
      },
      images: {
        get() {
          return this.node.attrs.images || []
        },
        set(images) {
          this.updateAttrs({
            images,
          })
        },
      },
      line1: {
        get() {
          return this.node.attrs.line1 || 'Sponsored by'
        },
        set(line1) {
          this.updateAttrs({
            line1,
          })
        },
      },
      line2: {
        get() {
          return this.node.attrs.line2 || 'Munch.app'
        },
        set(line2) {
          this.updateAttrs({
            line2,
          })
        },
      },
    },
    data() {
      return {
        state: null
      }
    },
    methods: {
      onImage(image) {
        this.images = [image]
        this.state = null
      }
    }
  }
</script>

<style scoped lang="less">
  .Avatar {
  }

  input {
    outline: none;
    border: none;
    background: none;

    padding: 0;

    /*width: 150px;*/
    /*padding: 0 12px 0 12px;*/
    /*height: 32px;*/
  }
</style>
