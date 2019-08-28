<template>
  <div>
    <section class="Banner">
      <place-image-banner :images="images" @onClickImage="(index) => $refs.imageWall.onClickImage(index)"/>
    </section>

    <div class="Information container flex-justify-between relative">
      <div class="ContentBody flex-grow">
        <section class="Max720 mt-32">
          <!--          <place-status :place="place"/>-->

          <h1>{{place.name}}</h1>

          <div class="mt-16">
            <div class="m--6 flex-wrap" v-if="place.tags.length > 0">
              <div class="p-4" v-for="tag in place.tags" :key="tag.id">
                <div class="bg-steam border-3 p-4-12">{{tag.name}}</div>
              </div>
            </div>
          </div>
        </section>

        <section class="Max720 mtb-32">
          <place-detail :place="place"/>
        </section>

        <section v-if="place.description" class="Max720">
          <div v-if="place.description" @click="expanded = !expanded" class="hover-pointer">
            <p class="Description" :class="{'text-ellipsis-4l': !expanded}">{{place.description}}</p>
          </div>
        </section>

        <section class="Max720 mtb-48">
          <h2>Location</h2>

          <p class="mt-16">{{place.location.address}}</p>

          <div class="mt-24 relative">
            <a class="index-content wh-100 absolute hover-pointer" target="_blank" :href="`https://www.google.com/maps?q=${place.location.latLng}`"
               style="height: 224px"></a>
            <google-embed-map :lat-lng="place.location.latLng" height="224"/>
          </div>
        </section>

      </div>

      <aside class="mt-24 flex-grow">
        <place-aside class="PlaceAside" :place="place"/>
      </aside>
    </div>
  </div>
</template>

<script>
  import PlaceAside from "../components/places/PlaceAside";
  import GoogleEmbedMap from "../components/core/GoogleEmbedMap";

  export default {
    components: {GoogleEmbedMap, PlaceAside},
    head() {
      const {image, name, description, slug, id} = this.place
      return this.$head({
        robots: {follow: true, index: true},
        canonical: `https://www.munch.app/${slug}-${id}`,
        graph: {
          title: `${name} · Munch`,
          description: description,
          type: 'place',
          image: image,
          url: `https://www.munch.app/${slug}-${id}`,
        },
        breadcrumbs: [
          {
            name: 'Places',
            item: `https://www.munch.app/places`
          },
          {
            name: name,
            item: `https://www.munch.app/${slug}-${id}`
          },
        ]
      })
    },
    asyncData({$api, params: {id}}) {
      return $api.get(`/migrations/places/${id}`)
        .then(({data: place}) => {
          return {place}
        })
    }
  }
</script>

<style scoped lang="less">
  .Information {
    @media (max-width: 991.98px) {
      flex-direction: column;
    }
  }

  .Max720 {
    max-width: 720px;
  }

  .ContentBody {
    @media (min-width: 992px) {
      margin-right: 48px;
    }
  }

  aside {
    max-width: 360px;
  }

  .PlaceAside {
    @media (max-width: 991.98px) {
      border-radius: 0;
      border-style: solid none none none;

      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
    }

    @media (min-width: 992px) {
      position: sticky;
      top: calc(24px + 72px /*Header72px*/);
    }
  }
</style>
