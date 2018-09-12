<script>
  import {Line, mixins} from 'vue-chartjs'

  export default {
    name: "SearchBarFilterPriceGraph",
    mixins: [mixins.reactiveProp],
    extends: Line,
    props: {
      priceGraph: {
        required: true
      },
      chartData: {
        required: false
      }
    },
    computed: {
      points() {
        if (this.priceGraph) {
          return this.priceGraph.points.map(point => ({y: point.count, x: point.price}))
        }
        return []
      },

      options() {
        return {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            xAxes: [{
              display: false,
            }],
            yAxes: [{
              display: false,
            }]
          },
          legend: {
            display: false
          },
          tooltips: {
            enabled: false
          }
        }
      },

      data() {
        return {
          labels: this.priceGraph.points.map(point => point.price),
          datasets: [{
            label: 'Filled',
            backgroundColor: '#EEDFDF',
            borderColor: '#EEDFDF',
            data: this.points,
            fill: true,
            pointRadius: 0,
            lineTension: 0.2,
            cubicInterpolationMode: 'monotone',
            spanGaps: false
          }]
        }
      }
    },
    mounted() {
      this.renderChart(this.data, this.options)
    }
  }
</script>
