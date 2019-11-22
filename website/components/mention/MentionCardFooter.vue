<template>
  <nuxt-link :to="`/@${profile.username}`" class="text-decoration-none">
    <div class="p-12 flex-align-center">
      <div class="wh-32px">
        <profile-pic :profile="profile"/>
      </div>
      <div class="ml-8 flex-shrink text-ellipsis-1l">
        <div class="tiny-bold b-a80">{{profile.name}}</div>
        <div class="flex-align-center">
          <simple-svg class="lh-0 wh-16px" fill="#444" :filepath="icon"/>
          <div class="ml-4 tiny b-a75">{{date}}</div>
        </div>
      </div>
    </div>
  </nuxt-link>
</template>

<script>
  import dateformat from 'dateformat'

  import ProfilePic from "../profile/ProfilePic";

  export default {
    name: "MentionCardFooter",
    components: {ProfilePic},
    props: {
      profile: {
        type: Object,
        required: true
      },
      icon: {
        type: String,
        required: true
      },
      millis: {}
    },
    computed: {
      date() {
        return dateformat(this.millis, 'mmm dd, yyyy')
      }
    }
  }
</script>

<style scoped>

</style>
