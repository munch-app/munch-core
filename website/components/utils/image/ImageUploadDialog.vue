<template>
  <div class="fixed position-0 bg-overlay flex-center">
    <div class="Dialog dialog-large flex-row wh-100">
      <div class="flex-column hr-right">
        <div class="flex-align-center flex-column p-16 hover-pointer" @click="tab = 'Upload'">
          <simple-svg class="wh-32px" fill="#444" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          <h6>UPLOAD</h6>
        </div>
        <div class="flex-align-center flex-column p-16 hr-top hover-pointer" @click="tab = 'Library'">
          <simple-svg class="wh-32px" fill="#444" :filepath="require('~/assets/icon/icons8-gallery.svg')"/>
          <h6>LIBRARY</h6>
        </div>
        <div class="flex-align-center flex-column p-16 hr-top hover-pointer" @click="tab = 'Library'">
          <simple-svg class="wh-32px" fill="#444" :filepath="require('~/assets/icon/icons8-gallery.svg')"/>
          <h6>INSTAGRAM</h6>
        </div>
      </div>

      <div class="p-24 wh-100">
        <div v-if="tab === 'Upload'">
          <div>
            <h3>Upload Image</h3>
            <div></div>
          </div>
          <div>

          </div>
        </div>
        <div v-else-if="tab === 'Library'" class="flex-column wh-100">
          <h3>Photo Library</h3>
          <div class="mt-8">
            <button class="tiny blue-outline">All</button>
            <button class="tiny border ml-8">Article</button>
            <button class="tiny border ml-8">Uploaded</button>
          </div>

          <div class="flex-grow">
            <div class="wh-100 flex-center">
              <p>No image found.</p>
            </div>
          </div>
        </div>
        <div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ImageUploadDialog",
    data() {
      return {
        tab: 'Upload'
      }
    },
    methods: {
      onImage(image) {
        this.$emit('on-image', image)
      }
    }
  }
</script>

<style scoped lang="less">
  .Dialog {
    padding: 0;
    height: 480px;
  }
</style>
