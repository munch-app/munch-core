<template>
  <div class="About">
    <section class="Munch">
      <div class="Brand container-1200 content">
        <div class="prismic-content">
          <div v-html="section1.heading"/>
          <div v-html="section1.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Vision bg-s050">
      <div class="content container-1200">
        <div class="content-image">
        </div>
        <div class="content-text prismic-content">
          <div v-html="section2.heading"/>
          <div v-html="section2.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Mission">
      <div class="content container-1200">
        <div class="content-image"/>
        <div class="content-text prismic-content">
          <div v-html="section3.heading"/>
          <div v-html="section3.paragraph"/>
        </div>
      </div>
    </section>

    <section class="Contact bg-p050">
      <div class="content container-1200">
        <div class="content-image"/>
        <div class="content-text prismic-content">
          <div v-html="section4.heading"/>
          <div v-html="section4.paragraph"/>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  export default {
    head() {
      return {title: 'About · Munch - Social Dining App'}
    },
    asyncData({$prismic}) {
      return $prismic.single('about').then(({data}) => {
        return {
          section1: {
            heading: $prismic.asHtml(data.heading_1),
            paragraph: $prismic.asHtml(data.paragraph_1),
            image: data.image_1.url
          },
          section2: {
            heading: $prismic.asHtml(data.heading_2),
            paragraph: $prismic.asHtml(data.paragraph_2),
            image: data.image_2.url
          },
          section3: {
            heading: $prismic.asHtml(data.heading_3),
            paragraph: $prismic.asHtml(data.paragraph_3),
            image: data.image_3.url
          },
          section4: {
            heading: $prismic.asHtml(data.heading_4),
            paragraph: $prismic.asHtml(data.paragraph_4),
            image: data.image_4.url
          }
        }
      })
    },
  }
</script>

<style scoped lang="less">
  section {
    padding-top: 64px;
    padding-bottom: 64px;
  }

  div.content {
    display: flex;
    flex: 1;

    img {
      width: 70%;
      padding-left: 64px;
      padding-right: 64px;
    }

    .content-image {
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }

    .content-text {
      flex: 0 0 50%;
    }

    @media (max-width: 767.98px) {
      flex-direction: column;

      img {
        width: 100%;
      }
    }
  }

  .Mission div.content {
    flex-direction: row-reverse;

    @media (max-width: 767.98px) {
      flex-direction: column;
    }

    .content-image {
      justify-content: flex-start;
    }
  }

  .Munch {
    background-size: cover;

    .Brand {
      display: flex;

      img {
        padding-left: 32px;
        padding-right: 32px;
      }
    }
  }
</style>
