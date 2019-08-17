<template>
  <div class="container-768 ptb-64">
    <div>
      <div class="flex-row">
        <div v-if="profile.image" class="ProfilePic flex-no-shrink">
          <cdn-img :image="profile.image"/>
        </div>
        <nuxt-link :to="`/me/edit`" v-else-if="isMe"
                   class="ProfilePic flex-no-shrink bg-steam flex-center">
          <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
        </nuxt-link>
        <div v-else class="ProfilePic flex-no-shrink bg-steam flex-center">
          <simple-svg class="wh-64px" fill="#ccc" :filepath="require('~/assets/icon/icons8-person.svg')"/>
        </div>

        <div class="ml-24">
          <div class="flex-align-center">
            <h1>{{profile.name}}</h1>
            <nuxt-link :to="`/me/edit`" class="flex-no-shrink ml-24" v-if="isMe">
              <button class="border tiny">Edit profile</button>
            </nuxt-link>
          </div>

          <div class="mt-8">
            <p class="b-a80" v-if="profile.bio">{{profile.bio}}</p>
            <nuxt-link :to="`/me/edit`" v-else-if="isMe" class="mb-8 text-decoration-none b-a60">
              Add bio...
            </nuxt-link>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-48">
      <div class="hr-bot">
        <div class="mr-16 mb-8 header hover-pointer">
          Articles
        </div>
      </div>

      <div class="mt-24">
        <div class="flex-center p-24">
          <p>This profile hasn't written any article yet.</p>
        </div>
      </div>
    </div>

    <div class="mt-48">

    </div>
  </div>
</template>

<script>
  import CdnImg from "../image/CdnImg";

  export default {
    name: "MeProfilePage",
    components: {CdnImg},
    computed: {
      isMe() {
        return this.$store.state.account.profile.id === this.profile.id
      }
    },
    props: {
      profile: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ProfilePic {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
  }

  button {
    margin-top: auto;
    margin-bottom: auto;
  }
</style>
