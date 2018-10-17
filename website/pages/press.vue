<template>
  <div class="container Press">
    <section class="prismic-content">
      <div v-html="fact_sheet.title"/>
      <div v-html="fact_sheet.content"/>
    </section>

    <section class="PressCoverage">
      <div v-html="press_coverage.title"/>
      <div class="press_coverage_list row">
        <div class="Card col-md-3 col-sm-6 col-12" v-for="item in pressCoverageList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.url" target="_blank">
            <img :src="item.image" class="border-3-top">
            <h4 class="text-ellipsis-1-line">{{item.name}}</h4>
          </a>
        </div>
      </div>
    </section>

    <section class="DigitalAsset">
      <div v-html="digital_asset.title"/>
      <div class="press_coverage_list row">
        <div class="Card col-md-3 col-12 col-sm-6" v-for="item in digitalAssetList" :key="item.url">
          <a class="elevation-2 elevation-hover-3 block border-3" :href="item.image" target="_blank">
            <img :src="item.image" class="border-3-top">
            <h4 class="text-ellipsis-1-line">{{item.name}}</h4>
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
      return {title: 'Press Kit · Munch'}
    },
    asyncData({Prismic}) {
      return Prismic.getSingle('press')
        .then(({data}) => {
          return {
            fact_sheet: {
              title: Prismic.asHtml(data.fact_sheet_title),
              content: Prismic.asHtml(data.fact_sheet_content),
            },
            press_coverage: {
              title: Prismic.asHtml(data.press_coverage_title),
              list: data.press_coverage_list
            },
            digital_asset: {
              title: Prismic.asHtml(data.digital_asset_title),
              list: data.digital_asset_list
            },
            contact_us: {
              title: Prismic.asHtml(data.contact_us_title),
              content: Prismic.asHtml(data.contact_us_content)
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

    .Card {
      margin-top: 16px;

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
