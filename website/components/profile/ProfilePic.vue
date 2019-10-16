<template>
  <div class="border-circle overflow-hidden bg-steam">
    <cdn-img v-if="image" :image="image" :type="type"/>
    <div v-else class="wh-100 aspect r-1-1">
      <div class="Icon" :class="iconClass">
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
      },
      iconClass: {
        type: String,
        default: 'bg-steam'
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
