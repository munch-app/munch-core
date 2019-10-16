<template>
  <div class="border-circle overflow-hidden">
    <cdn-img v-if="image" :image="image" :type="type"/>
    <div v-else class="wh-100 bg-steam aspect r-1-1">
      <div class="Icon">
        <simple-svg fill="#00000020" :filepath="require('~/assets/icon/icons8-person.svg')"/>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "ProfilePic",
    components: {CdnImg},
    props: {
      profile: {
        type: Object,
        required: true
      },
      type: {
        type: String,
        default: '320x320'
      }
    },
    computed: {
      image() {
        return this.profile?.image
      }
    }
  }
</script>

<style scoped lang="less">
  .Icon {
    padding: 20%;
  }
</style>
