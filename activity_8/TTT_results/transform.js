const fs = require('fs')

function transformTime (time) {
    return time.map(t => t / 1000)
}

function toDat (time) {
    let s = ""
    for (const t of time) {
        s += String(t) + "\n"
    }
    return s
}

const filePath = process.argv[2]

const data = JSON.parse(fs.readFileSync(filePath))

data.time = transformTime(data.time)

const datData = toDat(data.time)

fs.writeFileSync(
    filePath,
    JSON.stringify(
        data,
        null,
        2
    )
)

fs.writeFileSync(
    filePath.replace('json', 'dat'),
    datData
)
