<template>
  <div class="container-768 ptb-64">
    <div class="flex-row">
      <div class="ProfilePic flex-no-shrink hover-pointer" @click="onProfilePicStart">
        <cdn-img v-if="account.profile.image" :image="account.profile.image">
          <div class="flex-center bg-overlay">
            <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          </div>
        </cdn-img>
        <div v-else class="bg-steam flex-center">
          <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
        </div>
      </div>
      <div class="ml-24 w-100">
        <div>
          <input class="Name h1" v-model="account.profile.name" placeholder="Name"/>
        </div>
        <div class="flex-align-center">
          <span class="b-a60">https://www.munch.app/@</span>
          <input class="Name h6 flex-grow" v-model="account.profile.username" placeholder="username"/>
        </div>
      </div>
    </div>
    <div class="mt-24">
      <text-auto class="regular" v-model="account.profile.bio" placeholder="Add bio..."/>
    </div>
    <div class="mt-24">
      <button class="border" @click="$router.replace(`/@${$store.state.account.profile.username}`)">Cancel</button>
      <button class="ml-16 blue" @click="save">Save</button>
    </div>

    <div class="absolute" v-show="false">
      <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg" @change="onProfilePicFileChanged">
    </div>
  </div>
</template>

<script>
  import TextAuto from "../../components/core/TextAuto";
  import ImageUpload from "../../components/image/ImageUpload";
  import CdnImg from "../../components/image/CdnImg";

  export default {
    components: {CdnImg, ImageUpload, TextAuto},
    asyncData({$api}) {
      return $api.get('/me')
        .then(({data: account}) => {
          return {account}
        })
    },
    methods: {
      save() {
        const profile = this.account.profile
        this.$api.patch('/me', {
          profile: {
            username: profile.username,
            name: profile.name,
            bio: profile.bio,
          }
        }).then(({data: account}) => {
          this.$store.commit('account/setAccount', account)
          this.$store.dispatch('addMessage', {title: 'Updated profile'})
        }).catch(err => {
          this.$store.dispatch('addError', err)
        })
      },
      onProfilePicStart() {
        this.$refs.fileInput.click()
      },
      onProfilePicFileChanged(event) {
        const file = event.target.files[0]

        return this.$api.postImage(file, "PROFILE")
          .then(({data: image}) => {
            return this.onProfilePic(image)
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
      onProfilePic(image) {
        this.$api.patch('/me', {
          profile: {image}
        }).then(({data: account}) => {
          this.$store.commit('account/setAccount', account)
          this.$store.dispatch('addMessage', {title: 'Updated profile picture'})
        }).catch(err => {
          this.$store.dispatch('addError', err)
        })
      },

    }
  }
</script>

<style scoped lang="less">
  .ProfilePic > div {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
  }

  .Name {
    border: none;

    &:focus {
      outline: none;
      color: rgba(0, 0, 0, 0.75);
    }

    &::placeholder {
      color: rgba(0, 0, 0, 0.6);
    }
  }
</style>
