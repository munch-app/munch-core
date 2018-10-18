<template>
  <portal to="dialog">
    <h3 class="text-ellipsis-1-line">Add To Collection</h3>
    <div class="ListView">
      <div class="">
        <div class="Collection flex" v-for="collection in list" :key="collection.collectionId"
             @click="addToCollection(collection)">
          <image-size class="Image" :image="collection.image" v-if="collection.image"/>
          <div v-else class="Image whisper-200-bg"/>

          <div class="Content">
            <div class="Name text text-ellipsis-1-line">{{collection.name}}</div>
            <div class="small-bold text-ellipsis-1-line">{{collection.count}} places</div>
          </div>
        </div>
      </div>
      <div class="LoadingIndicator" v-if="more" v-observe-visibility="{callback:visibilityChanged,throttle:500}">
        <beat-loader color="#084E69" size="14px"/>
      </div>
    </div>
    <div>
      <button class="clear-elevated hover-pointer" @click="$emit('on-close')">Close</button>
    </div>
  </portal>
</template>

<script>
  import {mapGetters} from 'vuex'
  import ImageSize from "../core/ImageSize";

  export default {
    name: "ProfileCollectionAddPlace",
    components: {ImageSize},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      ...mapGetters('user/collections', ['list', 'more']),
    },
    methods: {
      visibilityChanged(isVisible) {
        if (isVisible) {
          this.$store.dispatch('user/collections/load')
        }
      },
      addToCollection(collection) {
        const collectionId = collection.collectionId
        const placeId = this.place.placeId
        this.$store.dispatch('user/collections/putItem', {collectionId, placeId})
          .then(() => {
            const message = `Added '${this.place.name}' to '${collection.name}' collection.`
            this.$store.dispatch('addMessage', {message})
          })
        this.$emit('on-close')
      }
    }
  }
</script>

<style scoped lang="less">
  h3 {
    padding-bottom: 12px;
  }

  button {
    float: right;
    margin-top: 16px;
  }

  .ListView {
    display: flex;
    flex-direction: column;

    margin-top: -16px;
    margin-bottom: -16px;

    max-height: 400px;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;

    .Collection {
      margin-top: 16px;
      margin-bottom: 16px;

      .Image {
        flex-shrink: 0;
        width: 50px;
        height: 50px;
        margin-right: 16px;
      }

      .Content {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .Name {
          margin-bottom: -2px;
        }
      }
    }
  }
</style>
