<template>
  <div class="container ptb-64">
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
      <div class="ml-24 w-100 flex-grow">
        <div>
          <input class="Name h1" v-model="account.profile.name" placeholder="Name"/>
        </div>
        <div class="flex-align-center">
          <span class="b-a60">https://www.munch.app/@</span>
          <input class="Name h6 flex-grow" v-model="account.profile.username" @keyup="onUsernameChange" placeholder="username"/>
        </div>
      </div>
    </div>
    <div class="mt-24">
      <text-auto class="regular" v-model="account.profile.bio" placeholder="Add bio..."/>
      <div v-if="account.profile.bio" class="tiny mb-4"
           :class="{error: account.profile.bio.length > 250}"
      >{{(account.profile.bio || "").length}}/250
      </div>
    </div>
    <div class="mt-24">
      <profile-link-input v-model="account.profile.links"/>
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
  import TextAuto from "../../components/utils/TextAuto";
  import CdnImg from "../../components/utils/image/CdnImg";
  import ProfileLinkInput from "../../components/profile/ProfileLinkInput";

  export default {
    components: {ProfileLinkInput, CdnImg, TextAuto},
    asyncData({$api}) {
      return $api.get('/me')
        .then(({data: account}) => {
          return {account}
        })
    },
    methods: {
      onUsernameChange() {
        const profile = this.account.profile
        profile.username = profile.username?.toLowerCase().replace(/[^0-9a-z]/, '').substring(0, 64)
      },
      save() {
        const profile = this.account.profile
        this.onPatchProfile({
          username: profile.username?.substring(0, 64),
          name: profile.name?.substring(0, 100),
          bio: profile.bio?.substring(0, 250),
          links: profile.links,
        })
      },
      onProfilePicStart() {
        this.$refs.fileInput.click()
      },
      onProfilePicFileChanged(event) {
        const file = event.target.files[0]

        return this.$api.postImage(file, "PROFILE")
          .then(({data: image}) => {
            return this.onPatchProfile({image})
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
      onPatchProfile(profile) {
        this.$api.patch('/me', {profile})
          .then(({data: account}) => {
            this.account = JSON.parse(JSON.stringify(account))
            this.$store.commit('account/setAccount', account)
            this.$store.dispatch('addMessage', {title: 'Updated Profile'})
          }).catch(err => {
          this.$store.dispatch('addError', err)
        })
      },
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
