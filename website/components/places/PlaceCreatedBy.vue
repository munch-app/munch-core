<template>
  <div class="flex-align-center border p-16 border-3">
    <nuxt-link :to="`/@${profile.username}`"
               class="text-decoration-none flex-no-shrink wh-40px border-circle overflow-hidden">
      <cdn-img v-if="profile.image" :image="profile.image" type="320x320"/>
      <div v-else class="wh-100 bg-blue"/>
    </nuxt-link>

    <div class="ml-16 flex-shrink text-ellipsis-1l">
      <nuxt-link :to="`/@${profile.username}`" class="text-decoration-none">
        <div class="mb weight-500 b-a80">{{profile.name}}</div>
      </nuxt-link>
      <div class="tiny b-a75 text-ellipsis-1l">{{profile.bio}}</div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "PlaceCreatedBy",
    components: {CdnImg},
    props: {
      profile: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped>

</style>
