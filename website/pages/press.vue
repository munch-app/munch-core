<template>
  <div class="container Press">
    <section class="prismic-content">
      <div v-html="fact_sheet.title"/>
      <div v-html="fact_sheet.content"/>
    </section>

    <section class="PressCoverage">
      <div v-html="press_coverage.title"/>

      <div class="PressCoverageList">
        <div class="Card" v-for="item in pressCoverageList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.url" target="_blank">
            <img :src="item.image" class="border-3-top">
            <h4 class="text-ellipsis-1l">{{item.name}}</h4>
          </a>
        </div>
      </div>
    </section>

    <section class="DigitalAsset">
      <div v-html="digital_asset.title"/>

      <div class="PressCoverageList">
        <div class="Card" v-for="item in digitalAssetList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.image" target="_blank">
            <img :src="item.image" class="border-3-top">
            <h4 class="text-ellipsis-1l">{{item.name}}</h4>
          </a>
        </div>
      </div>
    </section>

    <section>
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
  .Press {
    margin-bottom: 64px;
  }

  section {
    margin-top: 32px;
  }

  .PressCoverageList {
    display: flex;
    flex-wrap: wrap;
    margin-right: -12px;
    margin-left: -12px;

    .Card {
      margin-top: 16px;

      flex: 0 0 100%;
      max-width: 100%;
      padding-right: 12px;
      padding-left: 12px;

      @media (min-width: 576px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 768px) {
        flex: 0 0 25%;
        max-width: 25%;
      }

      h4 {
        padding: 12px 16px;
      }
    }
  }

  .PressCoverage {
    .Card img {
      object-fit: cover;
      height: 140px;
      width: 100%;
    }
  }

  .DigitalAsset {
    .Card img {
      padding: 16px;
      object-fit: contain;
      height: 150px;
      width: 100%;
    }
  }
</style>
