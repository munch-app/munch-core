<template>
  <div>
    <h2 class="mt-24">Award-Winning Places</h2>
    <p class="b-a75">If trophies were edible, you'd have em' at theses joints.</p>

    <horizontal-scroll-view class="CardList Square container-remove-gutter mt-24" :items="card.collections"
                            :map-key="a => a.collectionId"
                            :padding="24">
      <template slot-scope="{item}">
        <image-sizes class="CardItem overflow-hidden border-3" :sizes="item.image.sizes" width="1000">
          <div class="Overlay flex-center p-24 cubic-bezier hover-pointer" @click="onClick(item)">
            <h3 class="text-center white">
              {{item.name}}
            </h3>
          </div>
        </image-sizes>
      </template>
    </horizontal-scroll-view>
  </div>
</template>

<script>
  import HorizontalScrollView from "../../../core/HorizontalScrollView";
  import ImageSizes from "../../../core/ImageSizes";

  export default {
    name: "SearchCardHomeAwardCollection",
    components: {ImageSizes, HorizontalScrollView},
    props: {
      card: {
        type: Object,
        required: true
      }
    },
    methods: {
      onClick({collectionId}) {
        this.$router.push({path: `/collections/${collectionId}`})
      }
    }
  }
</script>

<style scoped lang="less">
  .Overlay {
    background: rgba(0, 0, 0, 0.5);

    &:hover {
      background: rgba(0, 0, 0, 0.6);
    }
  }
</style>
