<template>
  <div>
    <div class="container">
      <div class="Container flex">
        <div class="flex-grow">
          <div class="MediaBox flex-center overflow-hidden lh-0 bg-steam border-3" v-if="media.images.length">
            <cdn-img class="Media" :image="media.images[0]" type="1080x1080" object-fit="contain" :alt="title"/>
          </div>
        </div>

        <div class="Content">
          <div>
            <nuxt-link :to="`/@${media.profile.username}`"
                       class="text-decoration-none p-16 border-4 bg-steam flex-align-center">
              <profile-pic class="wh-40px" :profile="media.profile" icon-class="bg-white"/>
              <div class="ml-16">
                <h6>{{media.profile.name}}</h6>
                <p class="small">{{formatMillis(media.createdAt)}}</p>
              </div>
            </nuxt-link>

            <div class="mt-24 p-16 border-4 bg-steam">
              <template v-for="(node, index) in media.content">
                <div class="small lh-1-3 text-ellipsis-3l" v-if="node.type === 'text'" :key="index">
                  {{node.text}}
                </div>
              </template>
            </div>

            <div class="mt-24" v-for="mention in media.mentions" :key="mention.id">
              <nuxt-link :to="`/${mention.place.slug}-${mention.place.id}`"
                         class="block text-decoration-none">
                <div class="elevation-hover-2 p-16 bg-steam border-4">
                  <div class="flex-between">
                    <div class="mr-4">
                      <h5 class="pink text-ellipsis-1l">{{mention.place.name}}</h5>
                      <p class="small text-ellipsis-1l">
                        {{mention.place.location.address}}
                      </p>
                    </div>

                    <div class="p-8 bg-pink border-circle">
                      <simple-svg class="wh-20px" fill="#FFF" :filepath="require('~/assets/icon/icons8-map-pin.svg')"/>
                    </div>
                  </div>

                  <div class="mt-16" v-if="placeMedias.length">
                    <div class="m--6 flex-wrap">
                      <div class="p-6 flex-2" v-for="pMedia in placeMedias" :key="pMedia.id">
                        <profile-media :media="pMedia"/>
                      </div>
                    </div>
                  </div>
                </div>
              </nuxt-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <profile-footer :profile="media.profile" :extra="extra"/>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import CdnImg from "../../components/utils/image/CdnImg";
  import ProfilePic from "../../components/profile/ProfilePic";
  import ProfileFooter from "../../components/profile/ProfileFooter";
  import ProfileMedia from "../../components/profile/ProfileMedia";

  export default {
    components: {ProfileMedia, ProfileFooter, ProfilePic, CdnImg},
    head() {
      const {
        profile: {name, username},
        title, id, images, updatedAt, createdAt,
      } = this.media

      return this.$head({
        robots: {follow: true, index: true},
        url: `https://www.munch.app/@${username}/${id}`,
        canonical: `https://www.munch.app/@${username}/${id}`,
        title: `${this.title || 'Media'} - ${name} · Munch`,
        description: this.description,
        image: images && images[0],
        updatedAt: updatedAt,
        publishedAt: createdAt,
        authorName: name,
        breadcrumbs: [
          {
            name: name,
            item: `https://www.munch.app/@${username}`
          },
          {
            name: title || 'Media',
            item: `https://www.munch.app/@${username}/${id}`
          },
        ]
      })
    },
    asyncData({$api, error, params: {id}}) {
      return $api.get(`/medias/${id}`, {
        params: {
          fields: 'extra.profile.articles,extra.profile.medias,extra.place.medias'
        }
      }).then(({data: media, extra}) => {
        return {media, extra}
      }).catch(err => {
        if (err.response.status === 404) {
          return error({statusCode: 404, message: 'Media Not Found'})
        }
        throw err
      })
    },
    mounted() {
      const {profile: {username}, id} = this.media
      this.$path.replace({path: `/@${username}/${id}`})
    },
    computed: {
      title() {
        if (this.description) {
          return this.description.substring(0, 20).trim() + "..."
        }
      },
      description() {
        const textNodes = this.media.content
          .filter(n => n.type === 'text')

        if (textNodes.length) {
          return textNodes[0].text.substring(0, 80).trim()
        }
      },
      placeMedias() {
        return this.extra && this.extra['place.medias'] || []
      }
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
    }
  }
</script>

<style scoped lang="less">
  .Container {
    margin: 0 -24px;

    > * {
      padding: 0 24px;
    }
  }

  @media (max-width: 768px) {
    .Container {
      flex-wrap: wrap;

      padding-bottom: 64px;

      > * {
        padding-top: 24px;
      }
    }
  }

  @media (min-width: 768px) {
    .Container {
      padding-top: 48px;
      padding-bottom: 64px;
    }

    .MediaBox {
      height: calc(100vh - 72px/*Header72px*/ - 48px/*pt-48*/ - 64px /*pb-64px*/);
    }

    .Media {
      margin-left: auto;
      margin-right: auto;
    }

    .Content {
      flex: 0 0 40%;
      max-width: 40%;
    }
  }

  @media (min-width: 992px) {
    .Content {
      flex: 0 0 35%;
      max-width: 35%;
    }
  }

  @media (min-width: 1200px) {
    .Content {
      flex: 0 0 33.33%;
      max-width: 33.33%;
    }
  }
</style>
