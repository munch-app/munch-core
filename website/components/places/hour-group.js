export const Day = {
  mon: {
    text: "Monday", isToday() {
      return Day.isToday(Day.mon)
    }
  },
  tue: {
    text: "Tuesday", isToday() {
      return Day.isToday(Day.tue)
    }
  },
  wed: {
    text: "Wednesday", isToday() {
      return Day.isToday(Day.wed)
    }
  },
  thu: {
    text: "Thursday", isToday() {
      return Day.isToday(Day.thu)
    }
  },
  fri: {
    text: "Friday", isToday() {
      return Day.isToday(Day.fri)
    }
  },
  sat: {
    text: "Saturday", isToday() {
      return Day.isToday(Day.sat)
    }
  },
  sun: {
    text: "Sunday", isToday() {
      return Day.isToday(Day.sun)
    }
  },
  other: {
    text: "Other", isToday() {
      return Day.isToday(Day.other)
    }
  },
  today() {
    const today = new Date();
    switch (today.getDay()) {
      case 1:
        return Day.mon
      case 2:
        return Day.tue
      case 3:
        return Day.wed
      case 4:
        return Day.thu
      case 5:
        return Day.fri
      case 6:
        return Day.sat
      case 7:
      case 0:
        return Day.sun
      default:
        return Day.other
    }
  },
  isToday(day) {
    return day === Day.today()
  },
  parse(text) {
    switch (text.toLowerCase()) {
      case "mon":
        return Day.mon
      case "tue":
        return Day.tue
      case "wed":
        return Day.wed
      case "thu":
        return Day.thu
      case "fri":
        return Day.fri
      case "sat":
        return Day.sat
      case "sun":
        return Day.sun
      default:
        return Day.other
    }
  }
}
export const days = [Day.mon, Day.tue, Day.wed, Day.thu, Day.fri, Day.sat, Day.sun]

function parseTimeHuman(time) {
  if (time === '24:00' || time === '23:59') {
    return "Midnight"
  }
  const hm = time.split(':')
  const hour = parseInt(hm[0])
  const min = parseInt(hm[1])
  const minS = min < 10 ? '0' + min : min
  if (hour < 13) {
    return hour + ':' + minS + 'am'
  } else {
    return hour % 12 + ':' + minS + 'pm'
  }
}

function parseTimeInt(time) {
  const hm = time.split(':')
  const hour = parseInt(hm[0])
  const min = parseInt(hm[1])
  return (hour * 60) + min
}

export class Hour {
  constructor(day, open, close) {
    this.day = Day.parse(day)
    this.open = open
    this.close = close
  }

  timeRange() {
    return parseTimeHuman(this.open) + ' - ' + parseTimeHuman(this.close)
  }

  isBetween(date, opening, closing) {
    let day = new Date()
    const now = (day.getHours() * 60) + day.getMinutes()
    const open = parseTimeInt(this.open)
    const close = parseTimeInt(this.close)
    if (!opening) opening = 0
    if (!closing) closing = 0

    if (close < open) {
      return open - opening <= now && now + closing <= 1440
    }
    return open - opening <= now && now + closing <= close
  }
}

export class HourGroup {
  constructor(hours) {
    this.hours = hours
    this.dayHours = {}

    hours.sort(function (a, b) {
      return a.open - b.open
    })

    for (const i in hours) {
      if (hours.hasOwnProperty(i)) {
        const hour = this.hours[i]
        if (this.dayHours[hour.day.text]) {
          this.dayHours[hour.day.text] = this.dayHours[hour.day.text] + ', ' + hour.timeRange()
        } else {
          this.dayHours[hour.day.text] = hour.timeRange()
        }
      }
    }
  }

  getHour(day) {
    return this.dayHours[day.text] || "Closed"
  }

  isOpen(today) {
    if (!this.hours) {
      return 'none'
    }

    const date = new Date()
    if (!today) {
      today = Day.today()
    }

    for (const i in this.hours) {
      if (this.hours.hasOwnProperty(i) && this.hours[i].day === today) {
        const hour = this.hours[i]
        if (hour.isBetween(date)) {
          if (!hour.isBetween(date, 0, 30)) {
            return 'closing'
          }
          return 'open'
        } else if (hour.isBetween(date, 30)) {
          return 'opening'
        }
      }
    }
    return 'closed'
  }

  todayTimeRange() {
    let today = Day.today()
    return this.getHour(today)
  }
}
