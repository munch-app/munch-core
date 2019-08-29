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

          <div class="mt-8 BioRight">
            <p class="b-a80" v-if="profile.bio">{{profile.bio}}</p>
            <nuxt-link :to="`/me/edit`" v-else-if="isMe" class="mb-8 text-decoration-none b-a60">
              Add bio...
            </nuxt-link>
          </div>
        </div>
      </div>
      <div class="BioFullWidth mt-16" v-if="profile.bio">
        <p class="b-a80">{{profile.bio}}</p>
      </div>
    </div>

    <div class="mt-32" v-if="profile.links.length > 0">
      <div class="m--6 flex-wrap">
        <a class="p-6 block text-decoration-none black" v-for="link in profile.links" :key="link.uid"
           :href="link.url" target="_blank" rel="noreferrer nofollow noopener">
          <div class="bg-steam border-3 p-4-8 flex-align-center">
            <div class="p-2">
              <simple-svg v-if="link.type === 'INSTAGRAM'" class="wh-20px"
                          :filepath="require('~/assets/icon/icons8-instagram.svg')"/>
              <simple-svg v-else-if="link.type === 'FACEBOOK'" class="wh-20px"
                          :filepath="require('~/assets/icon/icons8-facebook.svg')"/>
              <simple-svg v-else class="wh-20px" :filepath="require('~/assets/icon/icons8-web.svg')"/>
            </div>
            <div>
              <div class="p-2 bold">{{link.name}}</div>
            </div>
          </div>
        </a>
      </div>
    </div>

    <div class="mt-48 pb-256">
      <div class="hr-bot">
        <div class="mr-16 mb-8 header hover-pointer">
          Articles
        </div>
      </div>

      <div class="mt-24">
        <div v-if="articles.length > 0">
          <div v-for="article in articles" :key="article.id" class="ptb-12">
            <article-card :article="article"/>
          </div>

          <div v-if="next">
            <div class="flex-center ptb-32" v-if="articleLoading">
              <beat-loader color="#07F" size="16px"/>
            </div>
            <div class="flex-center ptb-32" v-else>
              <button class="blue-outline" @click="onArticleLoadMore">Load more</button>
            </div>
          </div>
        </div>

        <div v-else>
          <div class="ptb-48 flex-center">
            <p>This profile hasn't written any article yet.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../components/utils/image/CdnImg";
  import ArticleCard from "../../components/article/ArticleCard";

  export default {
    components: {ArticleCard, CdnImg},
    validate({params: {username}}) {
      return /^[a-z0-9]{3,32}$/.test(_.toLower(username))
    },
    head() {
      const {name, username, bio, image} = this.profile
      return this.$head({
        robots: {follow: true, index: true},
        canonical: `https://www.munch.app/@${username}`,
        graph: {
          title: `${name} · Munch`,
          description: bio,
          type: 'profile',
          image: image,
          url: `https://www.munch.app/@${username}`,
        },
        breadcrumbs: [
          {
            name: name,
            item: `https://www.munch.app/@${username}`
          },
        ]
      })
    },
    asyncData({$api, params: {username}}) {
      return Promise.all([
        $api.get(`/profiles/${username}`)
          .then(({data: profile}) => ({profile})),
        $api.get(`/profiles/${username}/articles`, {params: {size: 10}})
          .then(({data: articles, cursor}) => ({articles, cursor}))
      ]).then(([{profile}, {articles, cursor}]) => {
        return {articles, profile, cursor}
      })
    },
    data() {
      return {
        articleLoading: false
      }
    },
    computed: {
      isMe() {
        const uid = this.$store.state.account.profile?.uid
        return uid && uid === this.profile.uid
      },
      next() {
        return this.cursor?.next
      }
    },
    methods: {
      onArticleLoadMore() {
        this.articleLoading = true

        this.$api.get(`/profiles/${this.profile.username}/articles`, {params: {size: 15, cursor: this.next}})
          .then(({data: articles, cursor}) => {
            this.articles.push(...articles)
            this.cursor = cursor
            this.articleLoading = false
          })
          .catch(error => this.$store.dispatch('addError', error))
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

    @media (max-width: 575.98px) {
      width: 64px;
      height: 64px;
    }
  }

  button {
    margin-top: auto;
    margin-bottom: auto;
  }

  .BioRight {
    @media (max-width: 575.98px) {
      display: none;
    }
  }

  .BioFullWidth {
    @media (min-width: 576px) {
      display: none;
    }
  }
</style>
