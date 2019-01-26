<template>
  <div class="container mb-64">
    <section class="prismic-content">
      <div v-html="fact_sheet.title"/>
      <div v-html="fact_sheet.content"/>
    </section>

    <section class="PressCoverage mt-64">
      <div v-html="press_coverage.title"/>

      <div class="PressCoverageList">
        <div class="Card" v-for="item in pressCoverageList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.url" target="_blank">
            <div class="aspect r-5-3 bg-whisper050 flex-center border-3 overflow-hidden">
              <img class="wh-100" :src="item.image">
            </div>
            <h5 class="mt-16 text-ellipsis-1l">{{item.name}}</h5>
          </a>
        </div>
      </div>
    </section>

    <section class="DigitalAsset mt-64">
      <div v-html="digital_asset.title"/>

      <div class="PressCoverageList">
        <div class="Card" v-for="item in digitalAssetList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.image" target="_blank">
            <div class="aspect r-1-1 bg-whisper050 flex-center border-3 overflow-hidden">
              <img class="wh-100" :src="item.image">
            </div>
            <h5 class="mt-16 text-ellipsis-1l">{{item.name}}</h5>
          </a>
        </div>
      </div>
    </section>

    <section class="mt-64">
      <div v-html="contact_us.title"/>
      <div v-html="contact_us.content"/>
    </section>
  </div>
</template>

<script>
  export default {
    head() {
      return {title: 'Press Kit · Munch Singapore'}
    },
    asyncData({$prismic}) {
      return $prismic.single('press').then(({data}) => {
        return {
          fact_sheet: {
            title: $prismic.asHtml(data.fact_sheet_title),
            content: $prismic.asHtml(data.fact_sheet_content),
          },
          press_coverage: {
            title: $prismic.asHtml(data.press_coverage_title),
            list: data.press_coverage_list
          },
          digital_asset: {
            title: $prismic.asHtml(data.digital_asset_title),
            list: data.digital_asset_list
          },
          contact_us: {
            title: $prismic.asHtml(data.contact_us_title),
            content: $prismic.asHtml(data.contact_us_content)
          }
        }
      })
    },
    computed: {
      pressCoverageList() {
        return this.press_coverage.list.map((item) => {
          return {
            url: item.press_coverage_url.url,
            image: item.press_coverage_image.url,
            name: item.press_coverage_article.length && item.press_coverage_article[0].text
          }
        })
      },
      digitalAssetList() {
        return this.digital_asset.list.map((item) => {
          return {
            image: item.digital_asset_image.url,
            name: item.digital_asset_name.length && item.digital_asset_name[0].text
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .PressCoverageList {
    display: flex;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;

    .Card {
      flex: 0 0 100%;
      max-width: 100%;
      padding: 12px;

      @media (min-width: 576px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 25%;
        max-width: 25%;
      }

      @media (min-width: 992px) {
        flex: 0 0 20%;
        max-width: 20%;
      }

      @media (min-width: 1200px) {
        flex: 0 0 16.666666667%;
        max-width: 16.666666667%;
      }

      a {
        padding: 24px 24px 8px 24px;
      }
    }
  }

  .PressCoverage {
    img {
      object-fit: cover;
    }
  }

  .DigitalAsset {
    img {
      object-fit: contain;
    }
  }
</style>
