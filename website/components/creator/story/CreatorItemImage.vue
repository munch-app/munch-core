<template>
  <div class="ContentImage">
    <div v-if="item.image">
      <image-sizes :sizes="item.image.sizes" width="1000" height="1000" object-fit="contain"/>
    </div>
    <div v-else class="bg-whisper100 p-16 flex-center hover-pointer" @click="onUploadImage">
      <div class="lh-1 weight-600 m-0">Upload Image</div>
    </div>

    <div class="absolute" v-show="false">
      <input ref="input" type="file" accept="image/x-png,image/gif,image/jpeg" @change="onFileChanged">
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import Vue from 'vue';
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "CreatorItemImage",
    components: {ImageSizes},
    props: {
      item: {
        type: Object,
        required: true,
        twoWay: true,
      }
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    methods: {
      onUploadImage() {
        this.$refs.input.click()
      },
      onFileChanged(event) {
        const file = event.target.files[0]

        const form = new FormData()
        form.append('file', file, file.name)
        return this.$axios.$post(`/files/creators/${this.creatorId}/images`, form)
          .then(({data}) => {
            console.log(data)

            Vue.set(this.item, 'image', data)
            this.$emit('change')
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">

</style>
