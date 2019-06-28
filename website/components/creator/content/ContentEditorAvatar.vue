<template>
  <div>
    <div class="relative">
      <content-avatar :images="images" :line1="line1" :line2="line2"/>
      <div class="Editing cubic-bezier absolute-0 flex-center">
        <div class="flex-center">
          <button @click="editing = true" class="small">Edit</button>
        </div>
      </div>
    </div>

    <portal to="dialog-w400" v-if="editing">
      <div v-on-clickaway="closeAll">
        <div class="">
          <button class="border w-100" @click="imageUpload">Upload Image</button>
        </div>
        <div class="mt-16">
          <input-text v-model="line1" label="Line 1"/>
        </div>
        <div class="mt-16">
          <input-text v-model="line2" label="Line 2"/>
        </div>

        <div class="mt-16 flex-end">
          <button class="secondary" @click="editing = false">Done</button>
        </div>


        <div class="absolute" v-show="false">
          <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg"
                 @change="(e) => imageOnFileChanged(e)">
        </div>

      </div>
    </portal>
  </div>
</template>

<script>
  import ContentPlace from "../../contents/ContentPlace";
  import ImageSizes from "../../core/ImageSizes";
  import ContentAvatar from "../../contents/ContentAvatar";
  import InputText from "../../core/InputText";

  export default {
    name: "ContentEditorPlace",
    components: {InputText, ContentAvatar, ImageSizes, ContentPlace},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      images: {
        get() {
          return this.node.attrs.images
        },
        set(images) {
          this.updateAttrs({
            images,
          })
        },
      },
      line1: {
        get() {
          return this.node.attrs.line1 || 'Line 1'
        },
        set(line1) {
          this.updateAttrs({
            line1,
          })
        },
      },
      line2: {
        get() {
          return this.node.attrs.line2 || 'Line 2'
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
        editing: false
      }
    },
    methods: {
      closeAll() {
        this.editing = false
      },
      imageUpdate(image) {
        this.images = [image]
        this.editing = false
      },
      imageUpload() {
        return this.$refs.fileInput.click()
      },
      imageOnFileChanged(event) {
        const file = event.target.files[0]

        const form = new FormData()
        form.append('file', file, file.name)

        return this.$api.post(`/creators/_/contents/_/images`, form)
          .then(({data}) => {
            console.log(data)
            this.imageUpdate(data)
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .Editing {
    background: rgba(0, 0, 0, 0.4);
    opacity: 0;

    &:hover {
      opacity: 1;
    }

    button {
      margin: 0 12px;
    }
  }
</style>
