<template>
  <div class="container pt-48 pb-64">
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

    <div class="mt-32" v-if="profile.links && profile.links.length > 0">
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

    <div class="mt-24">
      <div class="hr-bot ptb-16">
        <div class="flex-wrap m--8">
          <div class="p-8" v-for="nav in navigations" :key="nav.name" @click="onFocus(nav)">
            <div v-if="focused.name === nav.name"
                 class="border-3 p-6-12 hover-pointer border-black black">
              <div class="flex-center">
                <simple-svg fill="#000" class="wh-16px" :filepath="nav.icon"/>
                <div class="ml-4 bold">{{nav.name}}</div>
              </div>
            </div>
            <div v-else
                 class="border-3 p-6-12 hover-pointer border b-a75">
              <div class="flex-center">
                <simple-svg fill="rgba(0,0,0,0.75)" class="wh-16px" :filepath="nav.icon"/>
                <div class="ml-4 bold">{{nav.name}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="pb-256">
      <div class="mt-24" v-if="focused.path === ''">
        <div>
          <h4>{{profile.name}} articles</h4>
          <div></div>
        </div>

        <div>
          <h4>{{profile.name}} images</h4>
          <div></div>
        </div>
      </div>

      <div class="mt-48" v-else-if="focused.path === '/articles'">
        <div v-if="profile.articles.length > 0">
          <div class="flex-1-2-3 m--16">
            <div v-for="article in profile.articles" :key="article.id" class="p-16">
              <article-card :article="article"/>
            </div>
          </div>

          <div v-if="hasNext('next.articles')">
            <div class="flex-center ptb-32" v-if="loading.articles">
              <beat-loader color="#07F" size="16px"/>
            </div>
            <div class="flex-center ptb-32" v-else>
              <button class="blue-outline" @click="loadMoreArticles">Load more</button>
            </div>
          </div>
        </div>

        <div v-else>
          <div class="ptb-48 flex-center">
            <p><b>{{profile.name}}</b> hasn't written any article yet.</p>
          </div>
        </div>
      </div>

      <div class="mt-48" v-else-if="focused.path === '/medias'">
        <div v-if="profile.medias.length > 0">
          <div class="flex-1-2-3-4-5 m--12">
            <div v-for="media in profile.medias" :key="media.id" class="p-12">
              <pre class="overflow-hidden">{{media}}</pre>
            </div>
          </div>

          <div v-if="hasNext('next.medias')">
            <div class="flex-center ptb-32" v-if="loading.medias">
              <beat-loader color="#07F" size="16px"/>
            </div>
            <div class="flex-center ptb-32" v-else>
              <button class="blue-outline" @click="loadMoreMedias">Load more</button>
            </div>
          </div>
        </div>

        <div v-else>
          <div class="ptb-48 flex-center">
            <p><b>{{profile.name}}</b> hasn't uploaded any media yet.</p>
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
    head() {
      const {name, username, bio, image} = this.profile
      return this.$head({
        robots: {follow: true, index: true},
        type: 'profile',
        title: `${name}${this.focused.title}· Munch`,
        description: bio,
        image: image,
        url: `https://www.munch.app/@${username}`,
        canonical: `https://www.munch.app/@${username}`,
        breadcrumbs: [
          {
            name: name,
            item: `https://www.munch.app/@${username}`
          },
        ]
      })
    },
    asyncData({$api, params: {username}}) {
      return $api.get(`/profiles/${username}`, {params: {fields: 'articles,medias'}})
        .then(({data: profile, cursor}) => {
          return {profile, cursor}
        });
    },
    data() {
      const navigations = [
        {name: 'All', path: '', title: ' ', icon: require('~/assets/icon/icons8-collage.svg')},
        {name: 'Articles', path: '/articles', title: ' - Articles ', icon: require('~/assets/icon/icons8-web.svg')},
        {name: 'Images', path: '/medias', title: ' - Media ', icon: require('~/assets/icon/icons8-gallery.svg')}
      ]

      const routes = {
        '@:username': navigations[0],
        '@:username-articles': navigations[1],
        '@:username-medias': navigations[2],
      }

      console.log(this.$route.name)

      return {
        loading: {
          articles: false, medias: false
        },
        navigations,
        focused: routes[this.$route.name],
      }
    },
    computed: {
      isMe() {
        const uid = this.$store.state.account.profile?.uid
        return uid && uid === this.profile.uid
      },
    },
    methods: {
      onFocus(nav) {
        this.focused = nav

        const {username} = this.profile
        this.$path.replace({path: `/@${username}${nav.path}`})
      },
      hasNext(name) {
        if (this.cursor) {
          return this.cursor[name]
        }
      },
      loadMoreArticles() {
        this.loading.articles = true

        this.$api.get(`/profiles/${this.profile.username}/articles`, {
          params: {size: 15, cursor: this.cursor['next.articles']}
        }).then(({data: articles, cursor}) => {
          this.profile.articles.push(...articles)
          this.cursor['next.articles'] = cursor.next
          this.loading.articles = false
        }).catch(error => this.$store.dispatch('addError', error))
      },
      loadMoreMedias() {
        this.loading.medias = true

        this.$api.get(`/profiles/${this.profile.username}/medias`, {
          params: {size: 15, cursor: this.cursor['next.medias']}
        }).then(({data: medias, cursor}) => {
          this.profile.medias.push(...medias)
          this.cursor['next.medias'] = cursor.next
          this.loading.medias = false
        }).catch(error => this.$store.dispatch('addError', error))
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
