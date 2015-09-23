package edu.sjsu.cmpe275.lab1;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class FileServiceLoggingAspect {

	@Around("execution(* FileServiceImpl.readFile(..))")
	public void aroundRead(ProceedingJoinPoint joinPoint) throws Throwable {

		FileServiceImpl fileService = (FileServiceImpl) joinPoint.getTarget();
		Object[] args = joinPoint.getArgs();
		System.out.println(args[0] + " reads the file " + args[1]);

		if (!fileService.getFileMap().containsKey(args[0])) {

			System.out.println(args[0] + " does not exist!");
			return;
		}

		boolean okToRead = false;

		for (Textfile file : fileService.getFileMap().get(args[0])) {

			if (file.getFilepath() == args[1]) {

				System.out.println("Access granted to read the file.");
				okToRead = true;
				break;
			}
		}
		if (okToRead) {
			Object result = joinPoint.proceed();
		} else {

			System.out.println("Does not have the priviledge to read!");
			UnauthorizedException unauthorizedException = new UnauthorizedException(
					"Access Denied");
			throw unauthorizedException;
		}
	}

	@Around("execution(* FileServiceImpl.shareFile(..))")
	public void aroundShare(ProceedingJoinPoint joinPoint) throws Throwable {

		FileServiceImpl fileService = (FileServiceImpl) joinPoint.getTarget();
		Object[] args = joinPoint.getArgs();
		System.out.println(args[0] + " shares the file " + args[2] + " with "
				+ args[1]);

		if (!fileService.getFileMap().containsKey(args[0])) {

			System.out.println(args[0] + " does not exist");
			return;
		}

		Textfile tmp = null;
		for (Textfile file : fileService.getFileMap().get(args[0])) {

			if (file.getFilepath() == args[2]) {

				tmp = file;
				break;
			}
		}
		if (tmp != null) {

			System.out.println("Access granted for sharing the file.");
			Object result = joinPoint.proceed();
			// System.out.println("check"+ fileService.getFileMap());

		} else {

			System.out.println("Does not have the priviledge to share!");
			throw new UnauthorizedException(
					"Does not have the priviledge to share file!");
		}

	}

	@Around("execution(* FileServiceImpl.unshareFile(..))")
	public void aroundUnshare(ProceedingJoinPoint joinPoint) throws Throwable {

		FileServiceImpl fileService = (FileServiceImpl) joinPoint.getTarget();
		Object[] args = joinPoint.getArgs();
		System.out.println(args[0] + " unshares the file " + args[2] + " with "
				+ args[1]);

		if (!fileService.getFileMap().containsKey(args[0])) {

			System.out.println(args[0] + " does not exist!");
			return;
		}

		Textfile tmp = null;
		for (Textfile file : fileService.getFileMap().get(args[0])) {

			if (file.getFilepath() == args[2]) {

				tmp = file;
				break;
			}
		}

		if (tmp != null) {

			System.out.println("Acess granted to unshare the file.");
			Object result = joinPoint.proceed();

		} else {

			System.out.println("Does not have the priviledge to unshare!");
			throw new UnauthorizedException(
					"Does not have the priviledge to share!");
		}
	}
}
