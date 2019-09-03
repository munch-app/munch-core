<template>
  <div class="container ptb-24">
    <div class="bg-pink border-3 white mb-48">
      <div class="p-8-12 small">
        <b>NOTICE:</b> You are currently managing this profile through your admin escalation.
      </div>
    </div>

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
          <input class="Name h6 flex-grow" v-model="account.profile.username" @keyup="onUsernameChange"
                 placeholder="username"/>
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
      <button class="blue" @click="save">Update Profile</button>
    </div>

    <div class="mt-64" v-if="account.profile.uid">
      <h2>{{account.profile.name}} Articles</h2>

      <div class="mt-24 flex">
        <div>
          <nuxt-link :to="`/system/profiles/${account.profile.uid}/articles/drafts`">
            <button class="outline">Draft</button>
          </nuxt-link>
        </div>

        <div class="ml-16">
          <nuxt-link :to="`/system/profiles/${account.profile.uid}/articles/published`">
            <button class="outline">Published</button>
          </nuxt-link>
        </div>
      </div>
    </div>

    <div class="absolute" v-show="false">
      <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg" @change="onProfilePicFileChanged">
    </div>

  </div>
</template>

<script>
  import CdnImg from "../../../../components/utils/image/CdnImg";
  import TextAuto from "../../../../components/utils/TextAuto";
  import ProfileLinkInput from "../../../../components/profile/ProfileLinkInput";

  export default {
    layout: 'system',
    components: {ProfileLinkInput, TextAuto, CdnImg},
    asyncData({$api, params: {uid}}) {
      if (uid === 'new') {
        return {
          account: {
            profile: {
              username: '',
              name: '',
              bio: '',
            }
          }
        }
      }

      return $api.get(`/admin/profiles/${uid}`)
        .then(({data: profile}) => {
          return {account: {profile}}
        })
    },
    methods: {
      onUsernameChange() {
        const profile = this.account.profile
        profile.username = profile.username?.toLowerCase().replace(/[^0-9a-z]/, '').substring(0, 64)
      },
      save() {
        const profile = this.account.profile
        if (profile.uid) {
          this.$api.patch(`/admin/profiles/${profile.uid}`, profile)
            .then(({data: profile}) => {
              console.log(profile)

              this.account = {profile: JSON.parse(JSON.stringify(profile))}
              this.$store.dispatch('addMessage', {title: 'Admin System', message: 'Updated Profile'})
            })
            .catch(err => {
              this.$store.dispatch('addError', err)
            })
        } else {
          this.$api.post(`/admin/profiles`, profile)
            .then(({data: profile}) => {
              console.log(profile)

              this.account = {profile: JSON.parse(JSON.stringify(profile))}
              this.$store.dispatch('addMessage', {title: 'Admin System', message: 'Created Profile'})
            })
            .catch(err => {
              this.$store.dispatch('addError', err)
            })
        }
      },
      onProfilePicStart() {
        this.$refs.fileInput.click()
      },
      onProfilePicFileChanged(event) {
        const file = event.target.files[0]

        return this.$api.postImage(file, "PROFILE")
          .then(({data: image}) => {
            this.account.profile.image = image
            this.save()
          })
          .catch((err) => {
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
