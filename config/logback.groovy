import static java.nio.charset.Charset.forName

def APP_NAME = 'bloggy'
def ARCHIVE_FOLDER_NAME = APP_NAME + '-archive'

def LOG_FILE_DIR = '/home/alejandro/log/bloggy/'
def ARCHIVE_FILES_DIR = LOG_FILE_DIR + ARCHIVE_FOLDER_NAME + '/%d{yyyy.MM.dd}/'

def LOG_FILE_EXTENSION = '.log'
def ARCHIVE_FILE_NAME_PREFIX = '.%d{yyyy.MM.dd}_%i'
def ARCHIVE_FILE_NAME_EXTENSIOIN = '.log.zip'

def ENCODING = 'UTF-8'

def MAX_FILE_SIZE = '100MB'        // max log file size before archiving
def TOTAL_ARCHIVES_SIZE = '5GB'    // total size of all archive files
def MAX_ARCHIVES_HISTORY = 1000    // how many days keep the archive files

def LOG_PATTERN = '%date{dd.MM.yyyy HH:mm:ss.SSS} %-5level [%thread] [%logger{36}] %msg%n'

appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = forName(ENCODING)
        pattern = LOG_PATTERN
    }
}

appender('ROLLING_FILE', RollingFileAppender) {
    file = LOG_FILE_DIR + APP_NAME + LOG_FILE_EXTENSION
    append = true

    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = ARCHIVE_FILES_DIR + APP_NAME + ARCHIVE_FILE_NAME_PREFIX + ARCHIVE_FILE_NAME_EXTENSIOIN
        maxFileSize = MAX_FILE_SIZE
        totalSizeCap = TOTAL_ARCHIVES_SIZE
        maxHistory = MAX_ARCHIVES_HISTORY
    }

    encoder(PatternLayoutEncoder) {
        charset = forName(ENCODING)
        pattern = LOG_PATTERN
    }
}

root(ch.qos.logback.classic.Level.INFO, ['ROLLING_FILE', 'STDOUT'])
logger('com.gmail.aazavoykin', ch.qos.logback.classic.Level.DEBUG, ['ROLLING_FILE', 'STDOUT'], false)
logger('org.apache.http.wire', ch.qos.logback.classic.Level.DEBUG, ['ROLLING_FILE', 'STDOUT'], false)