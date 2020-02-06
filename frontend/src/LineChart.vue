<template>
  <div></div>
</template>

<script>
var Highcharts = require("highcharts/highstock");
export default {
  name: "Chart",
  props: {
    series: {
      type: Array,
      required: true
    }
  },
  data: function() {
    return {
      target: undefined
    };
  },
  methods: {
    addData: function(sensor, data) {
      this.target.series[sensor].addPoint(data);
    }
  },
  mounted: function() {
    this.target = Highcharts.stockChart(this.$el, {
      credits: false,
      //debugger;
      chart: {
        animation: Highcharts.svg,
        height: "500px"
      },
      xAxis: {
        type: "datetime"
      },
      title: {
        text: "Sensorendaten",
        x: -20 //center
      },
      rangeSelector: {
        enabled: false
      },
      subtitle: {
        text: "",
        x: -20
      },
      marker: {
        enabled: true
      },
      series: this.series
    });
  },
  beforeDestroy: function() {
    this.target.destroy();
  }
};
</script>