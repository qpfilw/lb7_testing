from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.appiumby import AppiumBy
import time
import logging
import sys

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

def setup_driver():
    options = UiAutomator2Options()
    options.platform_name = 'Android'
    options.platform_version = '14'
    options.device_name = 'emulator-5554'
    options.app_package = 'com.example.simplenotesapp'
    options.app_activity = '.MainActivity'
    options.automation_name = 'UiAutomator2'
    options.no_reset = True
    options.new_command_timeout = 300
    options.skip_server_installation = False
    options.uiautomator2_server_install_timeout = 60000
    
    try:
        driver = webdriver.Remote('http://127.0.0.1:4723', options=options)
        logger.info("Appium драйвер успешно инициализирован")
        return driver
    except Exception as e:
        logger.error(f"Ошибка при инициализации Appium драйвера: {str(e)}")
        sys.exit(1)

def main():
    driver = setup_driver()
    
    try:
        logger.info("Ожидание загрузки приложения")
        time.sleep(5)
        
        logger.info("Поиск поля ввода и ввод текста 'test note'")
        note_input = driver.find_element(AppiumBy.ID, 'com.example.simplenotesapp:id/noteEditText')
        note_input.clear()
        note_input.send_keys('test note')
        logger.info("Текст успешно введен")
        
        logger.info("Поиск кнопки 'Add' и выполнение тапа")
        add_button = driver.find_element(AppiumBy.ID, 'com.example.simplenotesapp:id/addButton')
        add_button.click()
        logger.info("Тап по кнопке 'Add' выполнен")
        
        logger.info("Ожидание 5 секунд для проверки результата")
        time.sleep(5)
        
    except Exception as e:
        logger.error(f"Ошибка при выполнении теста: {str(e)}")
    
    finally:
        logger.info("Закрытие Appium драйвера")
        driver.quit()

if __name__ == "__main__":
    main()